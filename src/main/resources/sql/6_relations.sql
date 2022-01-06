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
