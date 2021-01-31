package com.lambda.constant;

public final class JdbcConstant {

    public static final int TOKEN_DURATION = 24 * 60 * 60 * 1000;

    public static final String DEF_USERS_BY_USERNAME_FULL_QUERY = "SELECT username, " +
            "password, enabled, account_locked, account_expired, " +
            "credentials_expired FROM user WHERE username =?";


    public static final String DEF_USERS_BY_USERNAME_FULL_WITH_SETTING_QUERY = "SELECT u.id as id, u.username as username, password, enabled, account_locked, account_expired, credentials_expired,\n" +
            "       ga.authority as authority, s.id as setting_id, s.dark_mode as setting_dark_mode, avatar_url\n" +
            " FROM user u\n" +
            " INNER JOIN group_members gm ON gm.username = u.username\n" +
            " INNER JOIN `groups` g ON g.id = gm.group_id\n" +
            " INNER JOIN group_authorities ga on g.id = ga.group_id\n" +
            " LEFT JOIN user_profile up on u.id = up.user_id" +
            " LEFT JOIN setting s on u.id = s.user_id\n" +
            " WHERE u.username = ?;";

    public static final String DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority "
            + "from `groups` g, group_members gm, group_authorities ga "
            + "where gm.username = ? " + "and g.id = ga.group_id " + "and g.id = gm.group_id";
}
