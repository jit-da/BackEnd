package com.jitda.global.config.oauth2.userinfo;

import com.jitda.global.config.oauth2.userinfo.util.CastMapUtil;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    private Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        response = CastMapUtil.safeGetMap(attributes, "response");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getNickname() {
        return (String) response.get("name");
    }
}
