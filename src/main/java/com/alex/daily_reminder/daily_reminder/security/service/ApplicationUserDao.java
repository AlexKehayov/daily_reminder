package com.alex.daily_reminder.daily_reminder.security.service;

import com.alex.daily_reminder.daily_reminder.filter.UserFilter;
import com.alex.daily_reminder.daily_reminder.security.model.ApplicationUser;
import com.alex.daily_reminder.daily_reminder.security.model.ChangeUserDetailsDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);

    void saveUser(UserDTO userDTO) throws Exception;

    void updateUser(ChangeUserDetailsDTO changeUserDetailsDTO, SecurityUtil securityUtil) throws Exception;

    void saveUserEntity(UserEntity userEntity);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    List<UserEntity> selectUsers(UserFilter filter);

    int selectUsersCount(UserFilter filter);

    void deleteUser(String username);

}
