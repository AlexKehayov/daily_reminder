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
        userEntity.setGrantedAuthorities(JsonUtil.createJsonList(USER.getGrantedAuthorities().stream().toList()));
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
//        List<SimpleGrantedAuthority> grantedAuthorities = JsonUtil.readJsonList(userEntity.getGrantedAuthorities(), SimpleGrantedAuthority.class);
        applicationUser.setGrantedAuthorities(USER.getGrantedAuthorities()); //TODO FIX THIS
        return applicationUser;
    }
}
