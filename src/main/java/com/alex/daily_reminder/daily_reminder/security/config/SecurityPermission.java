package com.alex.daily_reminder.daily_reminder.security.config;

public enum SecurityPermission {
    ADMIN_ADMINISTRATE("admin:administrate"),
    USER_READ_DIARY("user:read_diary"),
    USER_WRITE_DIARY("user:write_diary"),
    USER_READ_ORGANIZER("user:read_organizer"),
    USER_WRITE_ORGANIZER("user:write_organizer");

    private final String permission;

    SecurityPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
