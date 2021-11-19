create table users
(
    username varchar(300) not null,
    password varchar(3000) not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(200) not null,
    is_account_non_expired boolean default true not null,
    is_account_non_locked boolean default true not null,
    is_credentials_non_expired boolean default true not null,
    is_enabled boolean default true not null,
    granted_authorities varchar(4000) null
);

create unique index users_username_uindex
    on users (username);

alter table users
    add constraint users_pk
        primary key (username);

