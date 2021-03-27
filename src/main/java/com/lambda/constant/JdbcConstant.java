package com.lambda.constant;

import lombok.Value;

@Value
public class JdbcConstant {

    public static final int TOKEN_DURATION = 24 * 60 * 60 * 1000;

    public static final String CUSTOM_SQL_STATE = "45000";

    public static final String DEF_USERS_BY_USERNAME_FULL_QUERY = "SELECT username, " +
            "password, enabled, account_locked, account_expired, " +
            "credentials_expired FROM user WHERE username =?";

    public static final String DEF_USERS_BY_USERNAME_FULL_WITH_SETTING_QUERY =
            " SELECT u.username as username, password, enabled, account_locked, account_expired, credentials_expired,\n" +
            " ga.authority as authority, s.alert as setting_alert, s.theme as setting_theme,\n" +
            " up.first_name, up.last_name, up.date_of_birth, up.gender, up.phone_number, up.email, up.avatar_url" +
            " FROM user u\n" +
            " INNER JOIN group_members gm ON gm.username = u.username\n" +
            " INNER JOIN `groups` g ON g.id = gm.group_id\n" +
            " INNER JOIN group_authorities ga on g.id = ga.group_id\n" +
            " LEFT JOIN user_profile up on u.username = up.username" +
            " LEFT JOIN setting s on u.username = s.username\n" +
            " WHERE u.username = ?";

    public static final String DEF_CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
            "select g.id, g.group_name, ga.authority "
            + "from `groups` g, group_members gm, group_authorities ga "
            + "where gm.username = ? " + "and g.id = ga.group_id " + "and g.id = gm.group_id";

    public static final String DEF_CUSTOM_FIND_GROUPS_SQL = "select group_name from `groups`";

    public static final String DEF_CUSTOM_FIND_GROUP_ID_SQL = "select id from `groups` where group_name = ?";

    public static final String DEF_CUSTOM_INSERT_GROUP_SQL = "insert into `groups` (group_name) values (?)";

    public static final String DEF_CUSTOM_RENAME_GROUP_SQL = "update `groups` set group_name = ? where group_name = ?";

    public static final String DEF_CUSTOM_DELETE_GROUP_SQL = "delete from `groups` where id = ?";

}
