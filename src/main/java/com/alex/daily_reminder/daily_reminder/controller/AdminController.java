package com.alex.daily_reminder.daily_reminder.controller;

import com.alex.daily_reminder.daily_reminder.filter.UserFilter;
import com.alex.daily_reminder.daily_reminder.security.config.SecurityPermission;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import com.alex.daily_reminder.daily_reminder.util.JsonUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final SecurityUtil securityUtil;
    private final ApplicationUserService applicationUserService;

    @GetMapping
    public String getAdminHome(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }

        UserFilter userFilter = new UserFilter();
        fillData(model, userFilter);
        return "admin/admin";
    }

    @PostMapping("/saveEntry")
    @ResponseBody
    public void saveUserEntry(
            @RequestParam String username,
            @RequestParam Boolean diaryRead,
            @RequestParam Boolean diaryWrite,
            @RequestParam Boolean organizerRead,
            @RequestParam Boolean organizerWrite,
            @RequestParam Boolean isEnabled) {

        UserEntity user = applicationUserService.findUserByUsername(username);

        String grantedAuthorities = user.getGrantedAuthorities();
        String[] split = grantedAuthorities.split(",");
        List<String> authorities = Arrays.stream(split).toList();
        Set<SimpleGrantedAuthority> grantedAuthoritiesSet = new HashSet<>();
        for (String role : authorities) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(role));
        }

        if (diaryRead) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(SecurityPermission.USER_READ_DIARY.getPermission()));
        } else {
            grantedAuthoritiesSet.remove(new SimpleGrantedAuthority(SecurityPermission.USER_READ_DIARY.getPermission()));
        }

        if (diaryWrite) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(SecurityPermission.USER_WRITE_DIARY.getPermission()));
        } else {
            grantedAuthoritiesSet.remove(new SimpleGrantedAuthority(SecurityPermission.USER_WRITE_DIARY.getPermission()));
        }

        if (organizerRead) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(SecurityPermission.USER_READ_ORGANIZER.getPermission()));
        } else {
            grantedAuthoritiesSet.remove(new SimpleGrantedAuthority(SecurityPermission.USER_READ_ORGANIZER.getPermission()));
        }

        if (organizerWrite) {
            grantedAuthoritiesSet.add(new SimpleGrantedAuthority(SecurityPermission.USER_WRITE_ORGANIZER.getPermission()));
        } else {
            grantedAuthoritiesSet.remove(new SimpleGrantedAuthority(SecurityPermission.USER_WRITE_ORGANIZER.getPermission()));
        }

        StringBuilder sb = new StringBuilder();
        for (SimpleGrantedAuthority sga : grantedAuthoritiesSet.stream().toList()){
            sb.append(sga.getAuthority());
            sb.append(",");
        }
        user.setGrantedAuthorities(sb.substring(0, sb.toString().length()-1));
        user.setEnabled(isEnabled);
        applicationUserService.saveUserEntity(user);
    }

    @PostMapping("/deleteEntry")
    @ResponseBody
    public void deleteEntry(
            @RequestParam String username) {
        applicationUserService.deleteUser(username);
    }

    @PostMapping("/searchUsers")
    public String searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer currentPage,
            Model model) {

        UserFilter filter = new UserFilter();

        if (StringUtils.hasText(username))
            filter.setUsername(username);

        if (StringUtils.hasText(firstName))
            filter.setFirstName(firstName);

        if (StringUtils.hasText(lastName))
            filter.setLastName(lastName);

        if (StringUtils.hasText(email))
            filter.setEmail(email);

        if (Objects.nonNull(currentPage))
            filter.setPage(currentPage);

        fillData(model, filter);

        return "admin/fragments/userTableContent.html :: table";
    }

    @PostMapping("/initConfigureModal")
    public String initConfigureModal(
            @RequestParam String username,
            Model model) {
        model.addAttribute("user", applicationUserService.findUserByUsername(username));
        return "admin/modals/configure :: configure";
    }

    private void fillData(Model model, UserFilter filter) {
        List<UserEntity> users = applicationUserService.selectUsers(filter);
        model.addAttribute("filter", filter);
        model.addAttribute("users", users);
        model.addAttribute("total", applicationUserService.selectUsersCount(filter));
    }

}
