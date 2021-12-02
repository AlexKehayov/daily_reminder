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