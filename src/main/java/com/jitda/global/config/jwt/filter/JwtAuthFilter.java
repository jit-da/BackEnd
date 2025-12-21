package com.jitda.global.config.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitda.domain.users.entity.User;
import com.jitda.domain.users.repository.UserRepository;
import com.jitda.global.config.jwt.service.JwtService;
import com.jitda.global.config.oauth2.CustomOAuth2User;
import com.jitda.global.response.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/login", "/api/v1/auth/login", "/api/v1/auth/signup", "/favicon.ico",
            "/api/v1/auth/reissue", "/swagger", "/swagger-ui.html",
            "/swagger-ui/index.html", "/swagger-ui", "/v3/api-docs", "/ws", "/default-ui.css");

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equals(request.getMethod())) {
            log.info("OPTIONS 요청, 인증을 통과하지 않음: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (isExcludedUrl(request.getRequestURI())) {
            log.info("필터 제외 대상 요청: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (accessToken != null) {
            checkAccessTokenAndAuthentication(accessToken, filterChain, request, response);
            return;
        }

        // AccessToken이 유효하지 않으면 클라이언트에 401 응답 전송
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "AccessToken is invalid");
    }

    private void checkAccessTokenAndAuthentication(String accessToken, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        jwtService.extractProviderUserId(accessToken).flatMap(userRepository::findByEmail).ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(User user) {
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                Map.of("email", user.getEmail()),
                "email",
                user
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContext Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
    }

    private boolean isExcludedUrl(String requestURI) {
        return EXCLUDED_URLS.stream().anyMatch(requestURI::startsWith);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.of(status, message, null, "E_AUTH");

        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
    }
}