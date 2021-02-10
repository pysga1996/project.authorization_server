package com.lambda.constant;

import lombok.Value;

@Value
public class UserColumn {

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String ENABLED = "enabled";

    public static final String ACCOUNT_EXPIRED = "account_expired";

    public static final String ACCOUNT_LOCKED = "account_locked";

    public static final String CREDENTIALS_EXPIRED = "credentials_expired";

    public static final String AUTHORITY = "authority";

    public static final String SETTING = "setting";
}
