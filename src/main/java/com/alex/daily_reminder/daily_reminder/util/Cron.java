package com.alex.daily_reminder.daily_reminder.util;

import com.alex.daily_reminder.daily_reminder.model.OrganizerRecordEntity;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import com.alex.daily_reminder.daily_reminder.service.OrganizerRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Cron {

    private final OrganizerRecordService organizerRecordService;
    private final ApplicationUserService userService;
    private final EmailUtil emailUtil;

    @Scheduled(cron = "0 18 14 * * *") //every day at 12:00 s/m/h/...      for every minute cron = "* */1 * * * *"
    public void sendReminders() {
        List<OrganizerRecordEntity> organizerRecordEntities = organizerRecordService.selectOrganizerRecordsForTomorrow();
        String subject = "A quick reminder from Daily Reminder :)";
        String text;
        for (OrganizerRecordEntity ore : organizerRecordEntities) {
            text = "You have the following task for tomorrow:\nTitle: " + ore.getTitle() + "\n" + "Content: " + ore.getContent() + "\n" + "Time: " + (ore.getIsFixedTime() ? ore.getFixedTime() : (ore.getFromTime() + " - " + ore.getToTime())) + "\nPlace: " + ore.getGeoPlace() + "\n\nSincerely yours, Daily Reminder ";
            UserEntity user = userService.findUserByUsername(ore.getUserUsername());
            emailUtil.sendEmail(subject, text, user.getEmail());
            System.out.println(1);
        }
    }
}
