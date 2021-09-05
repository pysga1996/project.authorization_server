package com.lambda.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {

    private int code;

    private String message;

    private String detail;

    private String timestamp;

    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
