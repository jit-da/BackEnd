package com.jitda.api.controller.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenResponseBuilder builder() {
        return new TokenResponseBuilder();
    }

    public static class TokenResponseBuilder {
        private String accessToken;
        private String refreshToken;

        public TokenResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public TokenResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenResponse build() {
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.accessToken = this.accessToken;
            tokenResponse.refreshToken = this.refreshToken;
            return tokenResponse;
        }
    }
}
