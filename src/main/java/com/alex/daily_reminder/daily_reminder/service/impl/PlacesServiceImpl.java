package com.alex.daily_reminder.daily_reminder.service.impl;

import com.alex.daily_reminder.daily_reminder.model.PlaceEntity;
import com.alex.daily_reminder.daily_reminder.repository.PlacesRepositoryCustom;
import com.alex.daily_reminder.daily_reminder.service.PlacesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlacesServiceImpl implements PlacesService {

    private final PlacesRepositoryCustom placesRepositoryCustom;

    @Override
    public List<PlaceEntity> autoCompletePlaces(String name) {
        return placesRepositoryCustom.autoCompletePlaces(name);
    }
}
