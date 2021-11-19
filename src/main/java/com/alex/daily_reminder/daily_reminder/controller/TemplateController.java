package com.alex.daily_reminder.daily_reminder.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping(value = {"/", "/home"})
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String getHomeView(){
        return "home";
    }

}
