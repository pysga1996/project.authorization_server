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
    token             mediumblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null
);


drop table if exists oauth_access_token;
create table oauth_access_token
(
    token_id          varchar(255) null,
    token             mediumblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null,
    authentication    mediumblob   null,
    refresh_token     varchar(255) null
);

drop table if exists oauth_refresh_token;
create table oauth_refresh_token
(
    token_id       varchar(255) null,
    token          mediumblob   null,
    authentication mediumblob   null
);

drop table if exists oauth_code;
create table oauth_code
(
    code           varchar(255) null,
    authentication mediumblob   null
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

# create table authorities (
#                              username varchar(50) not null,
#                              authority varchar(50) not null,
#                              constraint fk_authorities_users foreign key(username) references user(username)
# );

# create unique index ix_auth_username on authorities (username,authority);

drop table if exists `groups`;
create table `groups`
(
    id         bigint auto_increment
        primary key,
    group_name varchar(50) charset utf8 not null,
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
        foreign key (group_id) references `groups` (id)
);

drop table if exists group_members;
create table group_members
(
    username varchar(50) not null,
    group_id bigint      not null,
    primary key (group_id, username),
    constraint fk_group_members_group
        foreign key (group_id) references `groups` (id)
);

drop table if exists authentication_token;
create table authentication_token
(
    id          LONG auto_increment,
    token       VARCHAR(50) null,
    username    VARCHAR(50) null,
    create_date TIMESTAMP   not null,
    expire_date TIMESTAMP   not null,
    status      BIT(10)     null,
    constraint authentication_token_pk
        primary key (id)
)
    comment 'Authentication token management table';

create unique index authentication_token_token_uindex
    on authentication_token (token);

drop table if exists user;
create table user
(
    id                  bigint auto_increment not null primary key,
    account_expired     bit                   not null,
    account_locked      bit                   not null,
    credentials_expired bit                   not null,
    enabled             bit                   not null,
    password            varchar(255)          null,
    username            varchar(255)          null
);

drop table if exists setting;
create table setting
(
    id        bigint auto_increment not null
        primary key,
    dark_mode bit                   null,
    user_id   bigint                null
);

create index FKfpo2eyle43nv23h6531y0f8q6
    on setting (user_id);

drop table if exists user_profile;
create table user_profile
(
    id            BIGINT auto_increment not null primary key,
    user_id       BIGINT                null,
    first_name    NVARCHAR(50)          null,
    last_name     NVARCHAR(50)          null,
    date_of_birth TIMESTAMP             null,
    gender        BIT(10)               null,
    phone_number  VARCHAR(20)           null,
    email         VARCHAR(20)           null,
    avatar_url    VARCHAR(100)          null,
    constraint user_profile_pk
        primary key (id),
    constraint user_profile_user_id_fk
        foreign key (user_id) references user (id)
);

DELIMITER $$
DROP PROCEDURE IF EXISTS REGISTER $$
CREATE PROCEDURE REGISTER(IN username VARCHAR(255), IN password VARCHAR(255),
                          IN first_name VARCHAR(255), IN last_name VARCHAR(255), IN date_of_birth TIMESTAMP,
                          IN gender BIT(1), IN phone_number VARCHAR(255), IN email VARCHAR(255),
                          IN group_name VARCHAR(255))
proc: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            SHOW ERRORS;
            ROLLBACK;
            RESIGNAL;
        END;
    IF (EXISTS(SELECT id FROM user WHERE user.username = username)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'USERNAME_EXISTED';
    ELSE
        SET @id = (SELECT id FROM user WHERE user.username = username);
        INSERT INTO user (account_expired, account_locked, credentials_expired,
                          enabled, password, username)
        VALUES (true, true, true, false, password, username);
    END IF;
    #     SET @user_id = (SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'user'
#         AND TABLE_SCHEMA = (SELECT DATABASE()));
    SET @user_id = (SELECT LAST_INSERT_ID());
    IF (EXISTS(SELECT id FROM user_profile WHERE user_profile.user_id = @user_id)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'USER_PROFILE_EXISTED';
    ELSE
        INSERT INTO user_profile (username, first_name, last_name, date_of_birth, gender, phone_number, email)
        VALUES (@user_id, first_name, last_name, date_of_birth, gender, phone_number, email);
    END IF;
    IF (EXISTS(SELECT id FROM setting WHERE setting.user_id = @user_id)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'SETTING_EXISTED';
    ELSE
        INSERT INTO setting (alert, user_id)
        VALUES (0, @user_id);
    END IF;
    SET @check_group = (SELECT id FROM `groups` WHERE `groups`.group_name = group_name);
    IF (NOT ISNULL(@check_group)) THEN
        INSERT INTO group_members(username, group_id) VALUES (username, @check_group);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'GROUP_NOT_FOUND';
    END IF;
END $$
DELIMITER ;



