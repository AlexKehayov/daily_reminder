package com.alex.daily_reminder.daily_reminder.security.controller;

import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TemplateController {

    private final ApplicationUserService applicationUserService;

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping(value = { "/home"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getHomeView(){
        return "home";
    }

    @GetMapping("/registrationForm")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDTO userDto, ModelAndView mav,
            HttpServletRequest request,
            Errors errors) {

        try {
            applicationUserService.registerUser(userDto);
        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
            return mav;
        }

        return new ModelAndView("successRegister", "user", userDto);
    }

}
