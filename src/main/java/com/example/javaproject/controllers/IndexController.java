package com.example.javaproject.controllers;

import com.example.javaproject.models.User;
import com.example.javaproject.services.SecurityService;
import com.example.javaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
public class IndexController {

    private final SecurityService securityService;

    @Autowired
    public IndexController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(value={"/"})
    public String getIndexPage(Model model) {
        return "index";
    }

    @RequestMapping(value = {"/test"})
    public String test(Model model) {
        return "test";
    }

    @RequestMapping(value = {"/roles"})
    public String roles(Model model) {
        User currentUser = securityService.findLoggedInUser();
        if (currentUser == null) {
            currentUser = new User();
            currentUser.setRoles(Collections.emptySet());
        }
        model.addAttribute("roles", currentUser.getRoles());
        return "roles";
    }

    @RequestMapping(value = {"/admin"})
    public String admin(Model model) {
        return "admin";
    }
}
