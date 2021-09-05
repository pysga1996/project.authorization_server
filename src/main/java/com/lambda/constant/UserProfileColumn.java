package com.lambda.constant;

import lombok.Value;

@Value
public class UserProfileColumn {

    public static final String USERNAME = "username";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String GENDER = "gender";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String EMAIL = "email";
    public static final String AVATAR_URL = "avatar_url";

    private UserProfileColumn() {
    }


}
