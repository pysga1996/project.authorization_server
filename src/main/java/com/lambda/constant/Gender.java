package com.lambda.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum Gender {

    MALE(1),
    FEMALE(2),
    BISEXUAL(3),
    UNKNOWN(4);

    private static final Map<Integer, Gender> genderMap = new HashMap<>();

    static {
        for (Gender gender : Gender.values()) {
            genderMap.put(gender.value, gender);
        }
    }

    private final int value;

    Gender(int value) {
        this.value = value;
    }

    @JsonCreator
    public static Gender fromValue(Integer integer) {
        if (integer == null) {
            return Gender.UNKNOWN;
        }
        Gender gender = genderMap.get(integer);
        if (gender == null) {
            return Gender.UNKNOWN;
        }
        return gender;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

}
