package com.lambda.constant;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    USERNAME_EXISTED, USER_PROFILE_EXISTED, GROUP_NOT_FOUND,
    SETTING_EXISTED, USERNAME_NOT_FOUND, NONE;

    private static final Map<String, ErrorCode> valueMap = new HashMap<>();

    static {
        for (ErrorCode code: ErrorCode.values()) {
            valueMap.put(code.name(), code);
        }
    }

    public static ErrorCode get(String value) {
        return valueMap.get(value) == null ? NONE : valueMap.get(value);
    }
}
