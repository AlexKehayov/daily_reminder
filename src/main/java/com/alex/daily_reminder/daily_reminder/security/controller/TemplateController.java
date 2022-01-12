package com.alex.daily_reminder.daily_reminder.security.controller;

import com.alex.daily_reminder.daily_reminder.security.model.ChangeUserDetailsDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.model.UserEntity;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import com.alex.daily_reminder.daily_reminder.security.validator.ChangeUserDetailsValidator;
import com.alex.daily_reminder.daily_reminder.security.validator.RegistrationValidator;
import com.alex.daily_reminder.daily_reminder.util.EmailUtil;
import com.alex.daily_reminder.daily_reminder.util.SecurityUtil;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TemplateController {

    private final ApplicationUserService applicationUserService;
    private final SecurityUtil securityUtil;
    private final RegistrationValidator registrationValidator;
    private final ChangeUserDetailsValidator changeUserDetailsValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;

    @GetMapping
    public String getIndexView() {
        if (Objects.nonNull(securityUtil.getLoggedUser()))
            return "redirect:/home";
        else
            return "index";
    }

    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("/custom403")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getCustom403View(Model model) {
        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }
        return "custom403";
    }

    @GetMapping(value = {"/home"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getHomeView(Model model) {

        UserEntity loggedUser = securityUtil.getLoggedUser();
        if (Objects.nonNull(loggedUser)) {
            model.addAttribute("loggedUser", loggedUser);
        }

        return "home";
    }

    @GetMapping("/registrationForm")
    public String showRegistrationForm(Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") UserDTO userDto, Model model) {

        model.addAttribute("user", userDto);

        List<ValidationError> errors = registrationValidator.validate(userDto);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("errors", errors);
            return "register";
        } else {
            try {
                applicationUserService.registerUser(userDto);
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "register";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/changeUserDetailsForm")
    public String changeUserDetailsForm(Model model) {
        UserEntity loggedUser = securityUtil.getLoggedUser();
        ChangeUserDetailsDTO changeUserDetailsDTO = new ChangeUserDetailsDTO();
        changeUserDetailsDTO.setEmail(loggedUser.getEmail());
        model.addAttribute("userChangeDetails", changeUserDetailsDTO);
        return "changeUserDetails";
    }

    @PostMapping("/changeUserDetails")
    public String changeUserDetails(
            @ModelAttribute("userChangeDetails") ChangeUserDetailsDTO userChangeDetails, Model model) {

        model.addAttribute("userChangeDetails", userChangeDetails);

        List<ValidationError> errors = changeUserDetailsValidator.validate(userChangeDetails);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("errors", errors);
            return "changeUserDetails";
        } else {
            try {
                applicationUserService.updateUser(userChangeDetails, securityUtil);
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "changeUserDetails";
            }
        }

        return "redirect:/logout";
    }

    @GetMapping("/recoverPasswordForm")
    public String recoverPasswordForm(Model model) {
        model.addAttribute("recoveryEmail", "");
        return "recoverPassword";
    }

    @PostMapping("/recoverPassword")
    public String recoverPassword(
            @RequestParam(required = false) String recoveryEmail) {

        if (StringUtils.hasText(recoveryEmail)){
            UserEntity user = applicationUserService.findUserByEmail(recoveryEmail);

            if (Objects.nonNull(user)){
                Random r = new Random( System.currentTimeMillis() );
                int newIntPass = 10000 + r.nextInt(20000);
                String generatedPassword = Integer.toString(newIntPass);

                user.setPassword(passwordEncoder.encode(generatedPassword));
                applicationUserService.saveUserEntity(user);

                emailUtil.sendEmail("New password for DailyReminder", "Your new password: " + generatedPassword + ". Change it when you log in.", user.getEmail());
            }
        }

        return "redirect:/login";
    }

}
