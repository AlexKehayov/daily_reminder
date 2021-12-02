package com.alex.daily_reminder.daily_reminder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "places", schema = "daily_reminder")
public class PlaceEntity implements Serializable {
    @Id
    @Column(name = "location_name")
    private String locationName;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

}
