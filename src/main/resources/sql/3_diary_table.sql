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