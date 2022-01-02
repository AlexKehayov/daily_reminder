package com.alex.daily_reminder.daily_reminder.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@Table(name = "organizer_record", schema = "daily_reminder")
public class OrganizerRecordEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_username")
    private String userUsername;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "fixed_date")
    @Temporal(TemporalType.DATE)
    private Date fixedDate;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate;

    @Column(name = "fixed_time")
    private LocalTime fixedTime;

    @Column(name = "from_time")
    private LocalTime fromTime;

    @Column(name = "to_time")
    private LocalTime toTime;

    @Column(name = "geo_lat")
    private Double geoLat;

    @Column(name = "geo_lng")
    private Double geoLng;

    @Column(name = "geo_place")
    private String geoPlace;

    @Column(name = "is_fixed_date")
    private Boolean isFixedDate;

    @Column(name = "is_fixed_time")
    private Boolean isFixedTime;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "note")
    private String note;

    public OrganizerRecordEntity() {
        this.setIsFixedDate(true);
        this.setIsFixedTime(true);
        this.setIsDone(false);
    }
}
