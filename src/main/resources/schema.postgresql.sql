drop table if exists oauth_client_details;
create table oauth_client_details
(
    client_id               varchar(255)  not null
        primary key,
    resource_ids            varchar(255)  null,
    client_secret           varchar(255)  null,
    scope                   varchar(255)  null,
    authorized_grant_types  varchar(255)  null,
    web_server_redirect_uri varchar(255)  null,
    authorities             varchar(255)  null,
    access_token_validity   int           null,
    refresh_token_validity  int           null,
    additional_information  varchar(4096) null,
    autoapprove             varchar(255)  null
);


drop table if exists oauth_client_token;
create table oauth_client_token
(
    token_id          varchar(255) null,
    token             bytea   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null
);


drop table if exists oauth_access_token;
create table oauth_access_token
(
    token_id          varchar(255) null,
    token             bytea   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null,
    authentication    bytea   null,
    refresh_token     varchar(255) null
);

drop table if exists oauth_refresh_token;
create table oauth_refresh_token
(
    token_id       varchar(255) null,
    token          bytea   null,
    authentication bytea   null
);

drop table if exists oauth_code;
create table oauth_code
(
    code           varchar(255) null,
    authentication bytea   null
);

drop table if exists oauth_approvals;
create table oauth_approvals
(
    userId         varchar(255) null,
    clientId       varchar(255) null,
    scope          varchar(255) null,
    status         varchar(10)  null,
    expiresAt      timestamp    null,
    lastModifiedAt timestamp    null
);


-- create table authorities
-- (
--                                   username varchar(50) not null,
--                                   authority varchar(50) not null,
--                                   constraint fk_authorities_users foreign key(username) references user(username)
--      );
--

-- create unique index ix_auth_username on authorities (username, authority);

drop table if exists "groups";
create table "groups"
(
    id         bigserial
        primary key,
    group_name varchar(50) not null,
    constraint groups_group_name_uindex
        unique (group_name)
);

drop table if exists group_authorities;
create table group_authorities
(
    group_id  bigint      not null,
    authority varchar(50) not null,
    primary key (group_id, authority),
    constraint fk_group_authorities_group
        foreign key (group_id) references "groups" (id)
);

drop table if exists group_members;
create table group_members
(
    username varchar(50) not null,
    group_id bigint      not null,
    primary key (group_id, username),
    constraint fk_group_members_group
        foreign key (group_id) references "groups" (id)
);

drop table if exists authentication_token;
create table authentication_token
(
    id          bigserial,
    token       VARCHAR(50) null,
    username    VARCHAR(50) null,
    create_date TIMESTAMP   not null,
    expire_date TIMESTAMP   not null,
    status      BIT(10)     null,
    constraint authentication_token_pk
        primary key (id)
);
comment on  table authentication_token is 'Authentication token management table';

create unique index authentication_token_token_uindex
    on authentication_token (token);

drop table if exists user;
create table user
(
    account_expired     bit          not null,
    account_locked      bit          not null,
    credentials_expired bit          not null,
    enabled             bit          not null,
    password            varchar(255) null,
    username            varchar(255) not null primary key
);

drop table if exists setting;
create table setting
(
    username varchar(50) not null primary key,
    alert    bit         null,
    theme    varchar(10) null,
    constraint user_setting_username_fk
        foreign key (username) references user (username)
);

drop table if exists user_profile;
create table user_profile
(
    username      varchar(50)  not null primary key,
    first_name    varchar(50) null,
    last_name     varchar(50) null,
    date_of_birth TIMESTAMP    null,
    gender        BIT(10)      null,
    phone_number  VARCHAR(20)  null,
    email         VARCHAR(20)  null,
    avatar_url    VARCHAR(100) null,
    constraint user_profile_username_fk
        foreign key (username) references user (username)
);

CREATE OR REPLACE PROCEDURE REGISTER(IN p_username varchar(255), IN p_password varchar(255),
                          IN p_first_name varchar(255), IN p_last_name varchar(255),
                          IN p_date_of_birth timestamp, IN p_gender bit,
                          IN p_phone_number varchar(255), IN p_email varchar(255),
                          IN p_group_name varchar(255))
    LANGUAGE plpgsql
AS $$
DECLARE
    check_group int;
BEGIN
    IF (EXISTS(SELECT 1 FROM "user" u WHERE u.username = p_username)) THEN
        RAISE EXCEPTION USING
            ERRCODE = '45000',
            MESSAGE = 'USERNAME_EXISTED',
            HINT = 'User ' || p_username || ' does not exist';
    ELSE
        INSERT INTO "user"(account_expired, account_locked, credentials_expired,
                          enabled, password, username)
        VALUES (false, false, false, false, p_password, p_username);
    END IF;
    IF (EXISTS(SELECT 1 FROM user_profile WHERE user_profile.username = p_username)) THEN
        RAISE EXCEPTION USING
            ERRCODE = '45000',
            MESSAGE = 'USER_PROFILE_EXISTED',
            HINT = 'User profile ' || p_username || ' existed';
    ELSE
        INSERT INTO user_profile (username, first_name, last_name, date_of_birth, gender,
                                  phone_number, email)
        VALUES (p_username, p_first_name, p_last_name, p_date_of_birth, p_gender, p_phone_number,
                p_email);
    END IF;
    IF (EXISTS(SELECT 1 FROM setting WHERE setting.username = p_username)) THEN
        RAISE EXCEPTION USING
            ERRCODE = '45000',
            MESSAGE = 'SETTING_EXISTED',
            HINT = 'Setting ' || p_username || ' existed';
    ELSE
        INSERT INTO setting (username, alert, theme)
        VALUES (p_username, 1, 'light');
    END IF;
    SELECT id INTO check_group FROM "groups" WHERE "groups".group_name = p_group_name;
    IF (check_group IS NOT NULL) THEN
        INSERT INTO group_members(username, group_id) VALUES (p_username, check_group);
    ELSE
        RAISE EXCEPTION USING
            ERRCODE = '45000',
            MESSAGE = 'GROUP_NOT_FOUND',
            HINT = 'Group ' || p_group_name || ' did not found';
    END IF;
    COMMIT;
END $$
;


CREATE OR REPLACE PROCEDURE UNREGISTER(IN p_username VARCHAR(255))
    LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE FROM user_profile WHERE user_profile.username = p_username;
    DELETE FROM setting WHERE setting.username = p_username;
    DELETE FROM "user" WHERE "user".username = p_username;
    COMMIT;
END
$$;


CREATE OR REPLACE PROCEDURE DELETE_GROUP(p_group_name VARCHAR(255))
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_id int;
BEGIN
    SELECT g.id INTO v_id FROM "groups" g WHERE g.group_name = p_group_name;
    IF (v_id IS NULL) THEN
        RAISE EXCEPTION USING
            ERRCODE = '45000',
            MESSAGE = 'GROUP_NOT_FOUND',
            HINT = 'Not found groud id' || v_id;
    ELSE
        DELETE FROM group_members WHERE group_members.group_id = v_id;
        DELETE FROM group_authorities WHERE group_authorities.group_id = v_id;
        DELETE FROM "groups" WHERE "groups".id = v_id;
    END IF;
    COMMIT;
END
$$;




