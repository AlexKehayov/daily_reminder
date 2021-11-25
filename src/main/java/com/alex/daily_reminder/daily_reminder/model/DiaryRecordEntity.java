package com.alex.daily_reminder.daily_reminder.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "diary_record", schema = "daily_reminder")
public class DiaryRecordEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_username")
    private String userUsername;

    @Column(name = "content")
    private String content;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "geo_lat")
    private Float geoLat;

    @Column(name = "geo_lng")
    private Float geoLng;

    @Column(name = "geo_place")
    private String geoPlace;

}
