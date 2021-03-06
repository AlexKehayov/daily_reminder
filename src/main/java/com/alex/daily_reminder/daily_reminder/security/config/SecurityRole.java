package com.alex.daily_reminder.daily_reminder.security.config;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alex.daily_reminder.daily_reminder.security.config.SecurityPermission.*;

public enum SecurityRole {
    ADMIN(Sets.newHashSet(ADMIN_ADMINISTRATE)),
    USER(Sets.newHashSet(USER_READ_DIARY, USER_WRITE_DIARY, USER_READ_ORGANIZER, USER_WRITE_ORGANIZER));

    private final Set<SecurityPermission> permissions;

    SecurityRole(Set<SecurityPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SecurityPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
