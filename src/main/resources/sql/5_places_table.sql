create table places
(
    location_name varchar(500) not null,
    lat double not null,
    lng double not null,
    constraint places_pk
        primary key (location_name)
);

insert into places values ('Kardzhali', 41.638576, 25.373482);
insert into places values ('Sofia', 42.685984, 23.326714);
insert into places values ('Plovdiv', 42.144039, 24.744011);
insert into places values ('Stara Zagora', 42.422118, 25.632497);
insert into places values ('Burgas', 42.523568, 27.45523);

alter table organizer_record
    add is_done tinyint(1) null;

alter table organizer_record
    add note text null;