package com.alex.daily_reminder.daily_reminder.filter;

import java.util.HashMap;
import java.util.Map;

public class DiaryRecordSorterUtils {

    public static final String CREATED_DATE = "createdDate";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(CREATED_DATE, "d.created_date");
        return map;
    }

}
