package com.alex.daily_reminder.daily_reminder.util;

import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

@Component
@AllArgsConstructor
public class SecurityUtil {

    private final ApplicationUserService applicationUserService;

    public UserEntity getLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (StringUtils.hasText(username)){
            return applicationUserService.findUserByUsername(username);
        }

        return null;
    }

    public static boolean hasRole(String role){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Collection<? extends GrantedAuthority> authoritiesCollection = ((UserDetails) principal).getAuthorities();
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>(authoritiesCollection);
            for (GrantedAuthority ga : grantedAuthorities){
                if (ga.getAuthority().equals(role))
                    return true;
            }
        }
        return false;
    }

}
