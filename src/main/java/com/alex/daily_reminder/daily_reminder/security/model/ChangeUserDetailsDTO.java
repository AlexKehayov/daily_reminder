package com.alex.daily_reminder.daily_reminder.security.model;

import lombok.Data;

@Data
public class ChangeUserDetailsDTO {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String matchingNewPassword;

}
