package com.jitda.global.config.jwt.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitda.global.response.api.ApiResponse;
import com.jitda.global.response.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Authentication failed: {}", authException.getMessage());
        // Handle authentication failure
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.of(HttpStatus.UNAUTHORIZED, ExceptionCode.INVALID_ACCESS_TOKEN.getMessage(), null, ExceptionCode.INVALID_ACCESS_TOKEN.getCode());

        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
