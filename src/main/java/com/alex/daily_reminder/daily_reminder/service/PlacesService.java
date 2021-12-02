package com.alex.daily_reminder.daily_reminder.service;

import com.alex.daily_reminder.daily_reminder.model.PlaceEntity;

import java.util.List;

public interface PlacesService {

    List<PlaceEntity> autoCompletePlaces(String name);

}
