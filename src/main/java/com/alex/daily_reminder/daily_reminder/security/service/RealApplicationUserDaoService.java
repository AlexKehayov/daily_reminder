package com.alex.daily_reminder.daily_reminder.security.service;

import com.alex.daily_reminder.daily_reminder.security.mapper.UserMapper;
import com.alex.daily_reminder.daily_reminder.security.model.ApplicationUser;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.repository.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alex.daily_reminder.daily_reminder.security.config.SecurityRole.ADMIN;
import static com.alex.daily_reminder.daily_reminder.security.config.SecurityRole.USER;


@Repository("real")
public class RealApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public RealApplicationUserDaoService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<UserEntity> all = userRepository.findAll();
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        for (UserEntity userEntity : all) {
            applicationUsers.add(userMapper.entityToApplicationUser(userEntity));
        }
        return applicationUsers;
    }

    @Override
    public void saveUser(UserDTO userDTO) throws Exception {
        userRepository.save(userMapper.dtoToEntity(userDTO));
    }
}
