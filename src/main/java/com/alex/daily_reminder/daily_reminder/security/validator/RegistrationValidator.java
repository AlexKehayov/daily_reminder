package com.alex.daily_reminder.daily_reminder.security.validator;

import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationValidator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    public List<ValidationError> validate(UserDTO userDTO) {
        List<ValidationError> errors = new ArrayList<>();

        if (!StringUtils.hasText(userDTO.getFirstName()))
            errors.add(new ValidationError("firstName", "Empty field"));


        if (!StringUtils.hasText(userDTO.getLastName()))
            errors.add(new ValidationError("lastName", "Empty field"));


        if (!StringUtils.hasText(userDTO.getEmail())){
            errors.add(new ValidationError("email", "Empty field"));
        } else {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(userDTO.getEmail());
            if (!matcher.matches()){
                errors.add(new ValidationError("email", "Format is not valid"));
            }
        }

        if (!StringUtils.hasText(userDTO.getUsername()))
            errors.add(new ValidationError("username", "Empty field"));


        if (!StringUtils.hasText(userDTO.getPassword()))
            errors.add(new ValidationError("password", "Empty field"));


        if (!StringUtils.hasText(userDTO.getMatchingPassword())){
            errors.add(new ValidationError("matchingPassword", "Empty field"));
        } else {
            if (!userDTO.getPassword().equals(userDTO.getMatchingPassword())){
                errors.add(new ValidationError("matchingPassword", "Password not matching"));
            }
        }



        return errors;
    }

}
