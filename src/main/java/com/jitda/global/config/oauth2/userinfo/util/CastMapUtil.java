package com.jitda.global.config.oauth2.userinfo.util;

import java.util.Collections;
import java.util.Map;

public class CastMapUtil {

    private CastMapUtil() {}

    @SuppressWarnings("unchecked")
    public static Map<String, Object> safeGetMap(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Collections.emptyMap();
    }
}
