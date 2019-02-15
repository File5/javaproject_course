package com.example.javaproject.controllers;

import com.example.javaproject.models.Role;
import com.example.javaproject.models.RoleName;
import com.example.javaproject.models.User;
import com.example.javaproject.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UpdateUserValidator userValidator;
    private final SecurityService securityService;
    private final TemplateHelper templateHelper;

    public UserController(UserService userService, RoleService roleService, UpdateUserValidator userValidator, SecurityService securityService, TemplateHelper templateHelper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.securityService = securityService;
        this.templateHelper = templateHelper;
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

    @RequestMapping(value = {"/users/{username}/changeRole"}, method = RequestMethod.GET)
    public String getChangeRoleForm(Model model, @PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        boolean isAdmin = templateHelper.isAdmin(user);

        model.addAttribute("username", username);
        model.addAttribute("admin", isAdmin);

        return "users/change_role";
    }

    @RequestMapping(value = {"/users/{username}/changeRole"}, method = RequestMethod.POST)
    public String changeRole(Model model, @PathVariable("username") String username, @RequestParam(value = "admin", defaultValue = "false") boolean admin) {
        User user = userService.findByUsername(username);
        Role adminRole = roleService.findRoleByName(RoleName.ADMIN.name);

        boolean userIsAdmin = user.getRoles().contains(adminRole);

        if (userIsAdmin != admin) {
            if (admin) {
                roleService.addRoleToUser(adminRole, user);
            } else {
                roleService.removeRoleFromUser(adminRole, user);
            }
        }

        return "redirect:/users";
    }

    @RequestMapping(value = {"/users/{username}/delete"}, method = RequestMethod.GET)
    public String deleteUserConfirmation(Model model, @PathVariable("username") String username) {
        model.addAttribute("username", username);
        return "users/user_delete";
    }

    @RequestMapping(value = {"/users/{username}/delete"}, method = RequestMethod.POST)
    public String deleteUser(Model model, @PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            userService.delete(user);
        }
        return "redirect:/users";
    }
}
