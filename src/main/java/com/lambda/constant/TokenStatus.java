package com.lambda.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TokenStatus {
    INACTIVE(0),
    ACTIVE(1),
    USED(2),
    EXPIRED(3);

    private final int value;

    TokenStatus(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
