package com.alex.daily_reminder.daily_reminder.filter;

import java.util.HashMap;
import java.util.Map;

public class UserSorterUtils {

    public static final String USERNAME = "username";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(USERNAME, "d.username");
        return map;
    }

}
