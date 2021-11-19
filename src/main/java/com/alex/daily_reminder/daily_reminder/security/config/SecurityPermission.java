package com.alex.daily_reminder.daily_reminder.security.config;

public enum SecurityPermission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    SecurityPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
