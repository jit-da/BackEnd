package com.jitda.test.util;

import com.jitda.api.controller.auth.dto.request.LoginRequest;
import com.jitda.api.controller.auth.dto.request.SignUpRequest;
import com.jitda.domain.common.YN;

import java.lang.reflect.Field;

public class TestHelper {

    public static SignUpRequest createSignUpRequest(String email, String password, String passwordConfirm, 
                                                    String name, String phone, YN agreePrivacy, 
                                                    YN agreeUniqueInfo, YN agreeService) {
        SignUpRequest request = new SignUpRequest();
        setField(request, "email", email);
        setField(request, "password", password);
        setField(request, "passwordConfirm", passwordConfirm);
        setField(request, "name", name);
        setField(request, "phone", phone);
        setField(request, "agreePrivacy", agreePrivacy);
        setField(request, "agreeUniqueInfo", agreeUniqueInfo);
        setField(request, "agreeService", agreeService);
        return request;
    }

    public static LoginRequest createLoginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        setField(request, "email", email);
        setField(request, "password", password);
        return request;
    }

    private static void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}

