package com.lambda.constant;

//import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonValue;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TokenType {

    REGISTRATION(1),
    RESET_PASSWORD(2);

    private final int value;

    TokenType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
