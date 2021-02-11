package com.lambda.util;

import lombok.Value;

import javax.servlet.http.Cookie;

@Value
public class CookieSecureCreator {

    public static Cookie create(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setComment("Form cached");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setVersion(1);
        return cookie;
    }
}
