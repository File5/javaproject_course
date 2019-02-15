package com.example.javaproject.controllers;

import com.example.javaproject.models.User;
import com.example.javaproject.services.SecurityService;
import com.example.javaproject.services.UpdateUserValidator;
import com.example.javaproject.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final UpdateUserValidator userValidator;
    private final SecurityService securityService;

    public UserController(UserService userService, UpdateUserValidator userValidator, SecurityService securityService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.securityService = securityService;
    }

    @RequestMapping(value = {"/account"}, method = RequestMethod.GET)
    public String account(Model model) {
        User activeUser = securityService.findLoggedInUser();
        model.addAttribute("user", activeUser);
        return "account/account";
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String saveAccount(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        User activeUser = securityService.findLoggedInUser();

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "account/account";
        }

        String newUsername = user.getUsername();
        boolean usernameChanged = false;
        if (!activeUser.getUsername().equals(newUsername)) {
            activeUser.setUsername(newUsername);
            usernameChanged = true;
        }
        activeUser.setRoles(new HashSet<>(activeUser.getRoles()));

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            activeUser.setPassword(user.getPassword());

            userService.save(activeUser);
        } else {
            userService.update(activeUser);
        }

        if (usernameChanged) {
            securityService.autologin(activeUser.getUsername(), activeUser.getPassword());
        }

        return "account/account";
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public String listOfUsers(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/user_list";
    }
}
