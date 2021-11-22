package com.alex.daily_reminder.daily_reminder.security.mapper;

import com.alex.daily_reminder.daily_reminder.security.model.ApplicationUser;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alex.daily_reminder.daily_reminder.security.config.SecurityRole.USER;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity dtoToEntity(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);
        Set<SimpleGrantedAuthority> grantedAuthorities = USER.getGrantedAuthorities();
        StringBuilder sb = new StringBuilder();
        for (SimpleGrantedAuthority sga : grantedAuthorities){
            sb.append(sga.getAuthority());
            sb.append(",");
        }
        userEntity.setGrantedAuthorities(sb.substring(0, sb.toString().length()-1));
        return userEntity;
    }

    public ApplicationUser entityToApplicationUser(UserEntity userEntity){
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userEntity.getUsername());
        applicationUser.setPassword(userEntity.getPassword());
        applicationUser.setFirstName(userEntity.getFirstName());
        applicationUser.setLastName(userEntity.getLastName());
        applicationUser.setEmail(userEntity.getEmail());
        applicationUser.setAccountNonExpired(userEntity.isAccountNonExpired());
        applicationUser.setAccountNonLocked(userEntity.isAccountNonLocked());
        applicationUser.setCredentialsNonExpired(userEntity.isCredentialsNonExpired());
        applicationUser.setEnabled(userEntity.isEnabled());

        String grantedAuthorities = userEntity.getGrantedAuthorities();
        String[] split = grantedAuthorities.split(",");
        List<String> authorities = Arrays.stream(split).toList();
        Set<SimpleGrantedAuthority> grantedAuthoritiesSet = new HashSet<>();
        for (String role : authorities){
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(role));
        }
        applicationUser.setGrantedAuthorities(grantedAuthoritiesSet);
        return applicationUser;
    }
}
