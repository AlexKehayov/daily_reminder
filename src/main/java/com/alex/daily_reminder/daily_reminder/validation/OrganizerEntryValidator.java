package com.alex.daily_reminder.daily_reminder.validation;

import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class OrganizerEntryValidator {

    public List<ValidationError> validate(OrganizerRecordEntity organizerRecord) {
        List<ValidationError> errors = new ArrayList<>();

        if (!StringUtils.hasText(organizerRecord.getTitle()))
            errors.add(new ValidationError("title", "Empty field"));
        else {
            if (organizerRecord.getContent().length() > 300)
                errors.add(new ValidationError("title", "Text should be less than 300 symbols"));
        }

        if (!StringUtils.hasText(organizerRecord.getContent()))
            errors.add(new ValidationError("content", "Empty field"));
        else {
            if (organizerRecord.getContent().length() > 4000)
                errors.add(new ValidationError("content", "Text should be less than 4000 symbols"));
        }

        if (organizerRecord.getIsFixedDate()) {
            if (Objects.isNull(organizerRecord.getFixedDate()))
                errors.add(new ValidationError("fixedDate", "Empty field"));
        } else {
            if (Objects.isNull(organizerRecord.getFromDate()))
                errors.add(new ValidationError("fromDate", "Empty field"));
            if (Objects.isNull(organizerRecord.getToDate()))
                errors.add(new ValidationError("toDate", "Empty field"));
        }

        if (organizerRecord.getIsFixedTime()) {
            if (Objects.isNull(organizerRecord.getFixedTime()))
                errors.add(new ValidationError("fixedTime", "Empty field"));
        } else {
            if (Objects.isNull(organizerRecord.getFromTime()))
                errors.add(new ValidationError("fromTime", "Empty field"));
            if (Objects.isNull(organizerRecord.getToTime()))
                errors.add(new ValidationError("toTime", "Empty field"));
        }

        return errors;
    }

}
