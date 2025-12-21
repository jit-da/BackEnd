package com.jitda.global.config.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jitda.global.config.cookie.service.CookieService;
import com.jitda.global.config.redis.service.RedisService;
import com.jitda.global.response.exception.ExceptionCode;
import com.jitda.global.response.exception.RedisException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String PROVIDER_USER_ID_CLAIM = "providerUserId";
    private static final String PROVIDER_CLAIM = "provider";
    private static final String BEARER = "Bearer ";

    private final CookieService cookieService;
    private final RedisService redisService;


    /**
     * AccessToken 생성
     */
    public String createAccessToken(String email, String provider) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(PROVIDER_USER_ID_CLAIM, email+provider)
                .withClaim(PROVIDER_CLAIM, provider)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(String getProviderUserId) {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .withClaim(PROVIDER_USER_ID_CLAIM, getProviderUserId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * 헤더에 AccessToken 추가
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
    }

    /**
     * 헤더에 AccessToken + RefreshToken 추가
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, BEARER + accessToken);
        setRefreshTokenCookie(response, refreshToken);
    }

    /**
     * 헤더에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }


    public Optional<String> extractRefreshTokenFromCookie(HttpServletRequest request) {
        return cookieService.getRefreshTokenFromCookie(request);
    }

    /**
     * AccessToken 에서 ProviderUserId 추출
     */
    public Optional<String> extractProviderUserId(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(PROVIDER_USER_ID_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("엑세스 토큰에서 ProviderUserId 추출 실패. 타입: {}, 메시지: {}", e.getClass().getSimpleName(), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * RefreshToken 에서 ProviderUserId 추출
     */
    public Optional<String> extractProviderUserIdFromRefreshToken(String refreshToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(refreshToken)
                    .getClaim(PROVIDER_USER_ID_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("리프레시 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * AccessToken 에서 Provider 추출
     */
    public Optional<String> extractProvider(String accessToken) {
        try {
            return Optional.of(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(PROVIDER_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            String providerUserId = JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(PROVIDER_USER_ID_CLAIM)
                    .asString();
            String provider = JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(PROVIDER_CLAIM)
                    .asString();
            return Optional.of(providerUserId.replace(provider, ""));
        } catch (Exception e) {
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public Long getExpiration(String accessToken) {
        Date expiresAt = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getExpiresAt();
        return expiresAt.getTime() - new Date().getTime();
    }


    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        cookieService.addRefreshTokenCookie(response, refreshToken);
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. 타입: {}, 메시지: {}", e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    /**
     * Redis에 RefreshToken 저장 (ProviderUserId 키로 사용)
     */
    public void saveRefreshTokenToRedis(String getProviderUserId, String refreshToken) {
        if (refreshToken.length() > 4096 || getProviderUserId.length() > 256) {
            throw new RedisException(ExceptionCode.REDIS_DATA_SIZE_EXCEEDED_ERROR);
        }

        // 이메일을 키로 사용하여 RefreshToken 저장
        redisService.setValues(getProviderUserId, refreshToken, Duration.ofDays(TimeUnit.MILLISECONDS.toSeconds(refreshTokenExpirationPeriod)));
    }

    /**
     * Redis에서 RefreshToken 조회
     */
    public Optional<String> getRefreshTokenFromRedis(String getProviderUserId) {
        try {
            String refreshToken = redisService.getValues(getProviderUserId);
            return Optional.ofNullable(refreshToken);
        } catch (Exception e) {
            log.error("Redis에서 RefreshToken 조회 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Redis에서 RefreshToken 제거
     */
    public void removeRefreshTokenFromRedis(String getProviderUserId) {
        try {
            redisService.deleteValues(getProviderUserId);
            log.info("Redis에서 RefreshToken 제거 완료: {}", getProviderUserId);
        } catch (Exception e) {
            log.error("Redis에서 RefreshToken 제거 실패: {}", e.getMessage());
        }
    }
}
