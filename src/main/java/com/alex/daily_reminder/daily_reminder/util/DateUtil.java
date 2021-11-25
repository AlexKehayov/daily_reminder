package com.alex.daily_reminder.daily_reminder.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date toTheEndOfTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
}
