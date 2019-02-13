package com.example.javaproject.services;

import com.example.javaproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class UpdateUserValidator extends UserValidator {

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public UpdateUserValidator(UserService userService, SecurityService securityService) {
        super(userService);
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        checkValidString(errors, "username", user.getUsername(), 1, 32, "Size.userForm.username");

        User foundUser = userService.findByUsername(user.getUsername());
        if (foundUser != null && !foundUser.getUsername().equals(securityService.findLoggedInUsername())) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        String password = user.getPassword();
        if (password != null && !password.isEmpty()) {
            checkValidString(errors, "password", user.getPassword(), 4, 32, "Size.userForm.password");

            checkPasswordsMatch(errors, user);
        }
    }
}
