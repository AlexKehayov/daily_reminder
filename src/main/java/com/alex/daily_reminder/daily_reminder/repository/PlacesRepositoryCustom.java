package com.alex.daily_reminder.daily_reminder.repository;

import com.alex.daily_reminder.daily_reminder.model.PlaceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlacesRepositoryCustom {

    @Autowired
    private EntityManager em;

    public List<PlaceEntity> autoCompletePlaces(String name) {
        TypedQuery<PlaceEntity> query = em.createQuery("select x from PlaceEntity x where x.locationName like :name", PlaceEntity.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }

}
