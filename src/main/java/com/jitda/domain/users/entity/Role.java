package com.jitda.domain.users.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER"), SELLER("ROLE_SELLER"), ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
