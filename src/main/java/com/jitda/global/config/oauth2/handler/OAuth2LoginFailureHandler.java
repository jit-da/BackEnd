package com.jitda.global.config.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(OAuth2LoginFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2LoginSuccessHandler.onAuthenticationFailure() 실행 - OAuth2 로그인 실패 요청 진입");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("소셜 로그인 실패! 서버 로그를 확인해주세요.");
        log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
    }
}
