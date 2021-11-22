package com.alex.daily_reminder.daily_reminder.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
    private String pointer;
    private String message;
}
