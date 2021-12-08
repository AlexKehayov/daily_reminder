package com.alex.daily_reminder.daily_reminder.security.service;

import com.alex.daily_reminder.daily_reminder.filter.DiaryRecordFilter;
import com.alex.daily_reminder.daily_reminder.filter.UserFilter;
import com.alex.daily_reminder.daily_reminder.model.DiaryRecordEntity;
import com.alex.daily_reminder.daily_reminder.security.model.ChangeUserDetailsDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDao applicationUserDao;

    @Autowired
    public ApplicationUserService(@Qualifier("real") ApplicationUserDao applicationUserDao) {
        this.applicationUserDao = applicationUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao.selectApplicationUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found!", username)));
    }

    public void registerUser(UserDTO userDTO) throws Exception {
        applicationUserDao.saveUser(userDTO);
    }

    public void updateUser(ChangeUserDetailsDTO changeUserDetailsDTO, SecurityUtil securityUtil) throws Exception {
        applicationUserDao.updateUser(changeUserDetailsDTO, securityUtil);
    }

    public void saveUserEntity(UserEntity userEntity) {
        applicationUserDao.saveUserEntity(userEntity);
    }

    public UserEntity findUserByUsername(String username) {
        return applicationUserDao.findByUsername(username);
    }

    public UserEntity findUserByEmail(String email) {
        return applicationUserDao.findByEmail(email);
    }

    public List<UserEntity> selectUsers(UserFilter filter){
        return applicationUserDao.selectUsers(filter);
    }

    public int selectUsersCount(UserFilter filter){
        return applicationUserDao.selectUsersCount(filter);
    }

    public void deleteUser(String username){
        applicationUserDao.deleteUser(username);
    }
}
