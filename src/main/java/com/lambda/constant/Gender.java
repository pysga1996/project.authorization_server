package com.lambda.constant;

public enum Gender {
    MALE(1),
    FEMALE(2),
    BISEXUAL(3),
    UNKNOWN(4);

    private final int value;

    Gender(int value) {
        this.value = value;
    }
}
