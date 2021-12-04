package com.alex.daily_reminder.daily_reminder.security.validator;

import com.alex.daily_reminder.daily_reminder.security.model.ChangeUserDetailsDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.repository.UserRepository;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class ChangeUserDetailsValidator {

    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    public List<ValidationError> validate(ChangeUserDetailsDTO changeUserDetailsDTO) {
        List<ValidationError> errors = new ArrayList<>();
        UserEntity loggedUser = securityUtil.getLoggedUser();

        if (!StringUtils.hasText(changeUserDetailsDTO.getEmail())){
            errors.add(new ValidationError("email", "Empty field"));
        } else {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(changeUserDetailsDTO.getEmail());
            if (!matcher.matches()){
                errors.add(new ValidationError("email", "Format is not valid"));
            } else {
                if (!changeUserDetailsDTO.getEmail().equals(loggedUser.getEmail()) && !CollectionUtils.isEmpty(userRepository.findByEmail(changeUserDetailsDTO.getEmail()))){
                    errors.add(new ValidationError("email", "Email is already taken"));
                }
            }
        }

        if (!StringUtils.hasText(changeUserDetailsDTO.getOldPassword())){
            errors.add(new ValidationError("oldPassword", "Empty field"));
        } else {
            if (!passwordEncoder.matches(changeUserDetailsDTO.getOldPassword(), loggedUser.getPassword())){
                errors.add(new ValidationError("oldPassword", "Old password not matching"));
            }
        }

        if (!StringUtils.hasText(changeUserDetailsDTO.getNewPassword()))
            errors.add(new ValidationError("newPassword", "Empty field"));


        if (!StringUtils.hasText(changeUserDetailsDTO.getMatchingNewPassword())){
            errors.add(new ValidationError("matchingNewPassword", "Empty field"));
        } else {
            if (!changeUserDetailsDTO.getNewPassword().equals(changeUserDetailsDTO.getMatchingNewPassword())){
                errors.add(new ValidationError("matchingNewPassword", "Password not matching"));
            }
        }

        return errors;
    }

}
