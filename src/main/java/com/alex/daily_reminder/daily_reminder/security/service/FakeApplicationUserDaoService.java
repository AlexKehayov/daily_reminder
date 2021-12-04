package com.alex.daily_reminder.daily_reminder.security.service;

import com.alex.daily_reminder.daily_reminder.security.mapper.UserMapper;
import com.alex.daily_reminder.daily_reminder.security.model.ApplicationUser;
import com.alex.daily_reminder.daily_reminder.security.model.ChangeUserDetailsDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.repository.UserRepository;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.alex.daily_reminder.daily_reminder.security.config.SecurityRole.ADMIN;
import static com.alex.daily_reminder.daily_reminder.security.config.SecurityRole.USER;


@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
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
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "alex_admin",
                        "Alex", "Kehayov",
                        passwordEncoder.encode("alex_admin"),
                        "alex.jedi.98@gmail.com",
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "alex_user",
                        "Alex", "Kehayov",
                        passwordEncoder.encode("alex_user"),
                        "alex.jedi.98@gmail.com",
                        USER.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );

        return applicationUsers;
    }

    @Override
    public void saveUser(UserDTO userDTO) throws Exception {
        userRepository.save(userMapper.dtoToEntity(userDTO));
    }

    @Override
    public void updateUser(ChangeUserDetailsDTO changeUserDetailsDTO, SecurityUtil securityUtil) throws Exception {
        UserEntity loggedUser = securityUtil.getLoggedUser();
        loggedUser.setEmail(changeUserDetailsDTO.getEmail());
        loggedUser.setPassword(passwordEncoder.encode(changeUserDetailsDTO.getNewPassword()));
        userRepository.save(loggedUser);
    }

    @Override
    public void saveUserEntity(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByUsername(String username) {
        List<UserEntity> users = userRepository.findByUsername(username);
        return users.isEmpty()? null : users.get(0);
    }

    @Override
    public UserEntity findByEmail(String email) {
        List<UserEntity> users = userRepository.findByEmail(email);
        return users.isEmpty()? null : users.get(0);
    }
}
