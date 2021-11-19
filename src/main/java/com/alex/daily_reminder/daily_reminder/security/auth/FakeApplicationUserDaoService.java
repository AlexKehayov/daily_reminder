package com.alex.daily_reminder.daily_reminder.security.auth;

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

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
}
