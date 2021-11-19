package com.alex.daily_reminder.daily_reminder.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

public class JsonUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
    }

    public static <C> C readJson(String json, Class<C> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String createJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <C> List<C> readJsonList(String json, Class<C> clazz) {
        try {
            return objectMapper.readValue(json,new TypeReference<List<C>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String createJsonList(List<T> list) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
