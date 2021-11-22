package com.alex.daily_reminder.daily_reminder.security.controller;

import com.alex.daily_reminder.daily_reminder.security.model.UserDTO;
import com.alex.daily_reminder.daily_reminder.security.service.ApplicationUserService;
import com.alex.daily_reminder.daily_reminder.security.validator.RegistrationValidator;
import com.alex.daily_reminder.daily_reminder.validation.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TemplateController {

    private final ApplicationUserService applicationUserService;
    private final RegistrationValidator registrationValidator;

    @GetMapping
    public String getIndexView(){
        return "index";
    }

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
    public String registerUserAccount(
            @ModelAttribute("user") UserDTO userDto, Model model) {

        model.addAttribute("user", userDto);

        List<ValidationError> errors = registrationValidator.validate(userDto);
        if(!CollectionUtils.isEmpty(errors)){
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

}
