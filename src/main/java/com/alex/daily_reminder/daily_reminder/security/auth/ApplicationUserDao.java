package com.alex.daily_reminder.daily_reminder.security.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);

}
