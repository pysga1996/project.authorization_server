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
    account_expired     bit                   not null,
    account_locked      bit                   not null,
    credentials_expired bit                   not null,
    enabled             bit                   not null,
    password            varchar(255)          null,
    username            varchar(255)          not null primary key
);

drop table if exists setting;
create table setting
(
    username        varchar(50) not null primary key,
    alert bit                   null,
    theme varchar(10)           null,
    constraint user_setting_username_fk
        foreign key (username) references user (username)
);

drop table if exists user_profile;
create table user_profile
(
    username       varchar(50)          not null primary key,
    first_name    NVARCHAR(50)          null,
    last_name     NVARCHAR(50)          null,
    date_of_birth TIMESTAMP             null,
    gender        BIT(10)               null,
    phone_number  VARCHAR(20)           null,
    email         VARCHAR(20)           null,
    avatar_url    VARCHAR(100)          null,
    constraint user_profile_username_fk
        foreign key (username) references user (username)
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
    IF (EXISTS(SELECT 1 FROM user WHERE user.username = username)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'USERNAME_EXISTED';
    ELSE
        INSERT INTO user (account_expired, account_locked, credentials_expired,
                          enabled, password, username)
        VALUES (false, false, false, false, password, username);
    END IF;
    #     SET @user_id = (SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'user'
#         AND TABLE_SCHEMA = (SELECT DATABASE()));
    IF (EXISTS(SELECT 1 FROM user_profile WHERE user_profile.username = username)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'USER_PROFILE_EXISTED';
    ELSE
        INSERT INTO user_profile (username, first_name, last_name, date_of_birth, gender, phone_number, email)
        VALUES (username, first_name, last_name, date_of_birth, gender, phone_number, email);
    END IF;
    IF (EXISTS(SELECT 1 FROM setting WHERE setting.username = username)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'SETTING_EXISTED';
    ELSE
        INSERT INTO setting (username, alert, theme)
        VALUES (username, 1, 'light');
    END IF;
    SET @check_group = (SELECT id FROM `groups` WHERE `groups`.group_name = group_name);
    IF (NOT ISNULL(@check_group)) THEN
        INSERT INTO group_members(username, group_id) VALUES (username, @check_group);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'GROUP_NOT_FOUND';
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS UNREGISTER $$
CREATE PROCEDURE UNREGISTER(IN username VARCHAR(255))
proc: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            SHOW ERRORS;
            ROLLBACK;
            RESIGNAL;
        END;
    DELETE FROM user_profile WHERE user_profile.username = username;
    DELETE FROM setting WHERE setting.username = username;
    DELETE FROM user WHERE user.username = username;
END $$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS DELETE_GROUP $$
CREATE PROCEDURE DELETE_GROUP(IN groupName VARCHAR(255))
proc: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            SHOW ERRORS;
            ROLLBACK;
            RESIGNAL;
        END;
    SET @id = (SELECT id FROM `groups` WHERE `groups`.group_name = groupName);
    IF (ISNULL(@id)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'GROUP_NOT_FOUND';
    ELSE
        DELETE FROM group_members WHERE group_members.group_id = @id;
        DELETE FROM group_authorities WHERE group_authorities.group_id = @id;
        DELETE FROM `groups` WHERE `groups`.id = @id;
    END IF;
END $$
DELIMITER ;



