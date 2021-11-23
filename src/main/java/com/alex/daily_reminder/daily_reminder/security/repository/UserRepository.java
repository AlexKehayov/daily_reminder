package com.alex.daily_reminder.daily_reminder.security.repository;

import com.alex.daily_reminder.daily_reminder.repository.BaseRepository;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;

import java.util.List;

public interface UserRepository extends BaseRepository<UserEntity, String> {

    List<UserEntity> findByUsername(String username);
    List<UserEntity> findByEmail(String email);

}
