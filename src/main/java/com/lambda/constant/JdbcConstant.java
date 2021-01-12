package com.lambda.constant;

public final class JdbcConstant {

    public static final String DEF_USERS_BY_USERNAME_FULL_QUERY = "SELECT username, " +
            "password, enabled, account_locked, account_expired, " +
            "credentials_expired FROM user WHERE username =?";

    public static final String DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority "
            + "from `groups` g, group_members gm, group_authorities ga "
            + "where gm.username = ? " + "and g.id = ga.group_id " + "and g.id = gm.group_id";
}
