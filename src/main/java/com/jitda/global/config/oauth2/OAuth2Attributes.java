package com.jitda.global.config.oauth2;

import com.jitda.domain.users.entity.Provider;
import com.jitda.domain.users.entity.User;
import com.jitda.global.config.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.jitda.global.config.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.jitda.global.config.oauth2.userinfo.NaverOAuth2UserInfo;
import com.jitda.global.config.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {

    private String nameAttributeKey; 
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuth2Attributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuth2Attributes of(Provider provider,
                                      String userNameAttributeName, Map<String, Object> attributes) {

        if (provider == Provider.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }

        if(provider == Provider.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }

        return ofNaver(userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(OAuth2UserInfo oauth2UserInfo, Provider provider) {
        return User.builder()
                .email(oauth2UserInfo.getEmail())
                .provider(provider)
                .build();
    }

    public static Provider getProvider(String registrationId) {
        // Member 에서도 사용하기 위해 대문자로 변경
        if (registrationId != null) {
            registrationId = registrationId.toUpperCase();
        }

        if ("GOOGLE".equals(registrationId)) {
            return Provider.GOOGLE;
        }
        if ("KAKAO".equals(registrationId)) {
            return Provider.KAKAO;
        }
        if("NAVER".equals(registrationId)) {
            return Provider.NAVER;
        }
        return null;
    }

}

