package com.alex.daily_reminder.daily_reminder.filter;

import java.util.HashMap;
import java.util.Map;

public class OrganizerRecordSorterUtils {

    public static final String FIXED_DATE = "fixedDate";
    public static final String FROM_DATE = "fromDate";
    public static final String CREATED_DATE = "createdDate";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(FIXED_DATE, "d.fixed_date");
        map.put(FROM_DATE, "d.from_date");
        map.put(CREATED_DATE, "d.created_date");
        return map;
    }

}
