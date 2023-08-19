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
    token             longblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null
);


drop table if exists oauth_access_token;
create table oauth_access_token
(
    token_id          varchar(255) null,
    token             longblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null,
    authentication    longblob   null,
    refresh_token     varchar(255) null
);

drop table if exists oauth_refresh_token;
create table oauth_refresh_token
(
    token_id       varchar(255) null,
    token          longblob   null,
    authentication longblob   null
);

drop table if exists oauth_code;
create table oauth_code
(
    code           varchar(255) null,
    authentication longblob   null
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


create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references `user` (username)
);


create unique index ix_auth_username on authorities (username, authority);

drop table if exists `groups`;
create table `groups`
(
    id         bigint auto_increment
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
    id          bigint auto_increment
        primary key,
    token       varchar(50)                           null,
    username    varchar(50)                           null,
    create_date timestamp default current_timestamp() not null on update current_timestamp(),
    expire_date timestamp default current_timestamp() not null on update current_timestamp(),
    status      bit(10)                               null
)
    comment 'Authentication token management table';

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

delimiter //
create or replace procedure register(in p_username varchar(255), in p_password varchar(255),
                          in p_first_name varchar(255), in p_last_name varchar(255),
                          in p_date_of_birth timestamp, in p_gender bit,
                          in p_phone_number varchar(255), in p_email varchar(255),
                          in p_group_name varchar(255))
begin
    declare check_group int;
    start transaction;
    if (exists(select 1 from `user` u where u.username = p_username)) then
        signal sqlstate '45000'
            set message_text = 'USERNAME_DOES_NOT_EXISTED';
    else
        insert into `user`(account_expired, account_locked, credentials_expired,
                          enabled, password, username)
        values (false, false, false, false, p_password, p_username);
    end if;
    if (exists(select 1 from user_profile where user_profile.username = p_username)) then
        signal sqlstate '45000'
            set message_text = 'USER_PROFILE_EXISTED';
    else
        insert into user_profile (username, first_name, last_name, date_of_birth, gender,
                                  phone_number, email)
        values (p_username, p_first_name, p_last_name, p_date_of_birth, p_gender, p_phone_number,
                p_email);
    end if;
    if (exists(select 1 from setting where setting.username = p_username)) then
        signal sqlstate '45000'
            set message_text = 'SETTING_EXISTED';
    else
        insert into setting (username, alert, theme)
        values (p_username, 1, 'light');
    end if;
    select id into check_group from `groups` where `groups`.group_name = p_group_name;
    if (check_group is not null) then
        insert into group_members(username, group_id) values (p_username, check_group);
    else
        signal sqlstate '45000'
            set message_text = 'GROUP_NOT_FOUND';
    end if;
    commit;
end //
delimiter ;

delimiter //
create or replace procedure unregister(in p_username varchar(255))
begin
    start transaction;
    delete from user_profile where user_profile.username = p_username;
    delete from setting where setting.username = p_username;
    delete from `user` where `user`.username = p_username;
    commit;
end //
delimiter ;

delimiter //
create or replace procedure delete_group(p_group_name varchar(255))
begin
    declare v_id int;
    start transaction;
    select g.id into v_id from `groups` g where g.group_name = p_group_name;
    if (v_id is null) then
        signal sqlstate '45000'
            set message_text = 'group_not_found';
    else
        delete from group_members where group_members.group_id = v_id;
        delete from group_authorities where group_authorities.group_id = v_id;
        delete from `groups` where `groups`.id = v_id;
    end if;
    commit;
end //
delimiter ;




