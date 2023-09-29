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

create table diary_record
(
    id int auto_increment,
    user_username varchar(300) not null,
    content text not null,
    created_date datetime not null,
    geo_lat numeric null,
    geo_lng numeric null,
    geo_place varchar(1000) null,
    constraint diary_record_pk
        primary key (id)
);

alter table diary_record modify geo_lat double null;
alter table diary_record modify geo_lng double null;

create table organizer_record
(
    id int auto_increment,
    user_username varchar(300) not null,
    title varchar(300) not null,
    content text not null,
    created_date datetime not null,
    fixed_date date null,
    from_date date null,
    to_date date null,
    fixed_time time null,
    from_time time null,
    to_time time null,
    geo_lat double null,
    geo_lng double null,
    geo_place varchar(1000) null,
    constraint organizer_record_pk
        primary key (id)
);

alter table organizer_record
    add is_fixed_date boolean not null;

alter table organizer_record
    add is_fixed_time boolean not null;

create table places
(
    location_name varchar(500) not null,
    lat double not null,
    lng double not null,
    constraint places_pk
        primary key (location_name)
);

alter table organizer_record
    add is_done tinyint(1) null;

alter table organizer_record
    add note text null;

alter table diary_record
    add constraint diary_record_users_username_fk
        foreign key (user_username) references users (username)
            on update cascade on delete cascade;

alter table diary_record modify geo_place varchar(500) null;

alter table diary_record
    add constraint diary_record_places_location_name_fk
        foreign key (geo_place) references places (location_name);

alter table organizer_record
    add constraint organizer_record_users_username_fk
        foreign key (user_username) references users (username)
            on update cascade on delete cascade;

alter table organizer_record modify geo_place varchar(500) null;

alter table organizer_record
    add constraint organizer_record_places_location_name_fk
        foreign key (geo_place) references places (location_name);

alter table diary_record drop foreign key diary_record_places_location_name_fk;
drop index diary_record_places_location_name_fk on diary_record;
alter table organizer_record drop foreign key organizer_record_places_location_name_fk;
drop index organizer_record_places_location_name_fk on organizer_record;