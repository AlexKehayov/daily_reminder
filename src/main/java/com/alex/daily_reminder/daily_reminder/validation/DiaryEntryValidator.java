package com.alex.daily_reminder.daily_reminder.validation;

import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class DiaryEntryValidator {

    public List<ValidationError> validate(DiaryRecordEntity diaryRecord) {
        List<ValidationError> errors = new ArrayList<>();

        if (!StringUtils.hasText(diaryRecord.getContent()))
            errors.add(new ValidationError("content", "Empty field"));
        else{
            if (diaryRecord.getContent().length()>4000)
                errors.add(new ValidationError("content", "Text should be less than 4000 symbols"));
        }

        return errors;
    }

}
