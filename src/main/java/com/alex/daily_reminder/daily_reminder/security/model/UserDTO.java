package com.alex.daily_reminder.daily_reminder.security.model;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String matchingPassword;
    private String email;
}
