package com.example.javaproject.services;

import com.example.javaproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        checkValidString(errors, "username", user.getUsername(), 1, 32, "Size.userForm.username");

        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        checkValidString(errors, "password", user.getPassword(), 4, 32, "Size.userForm.password");

        checkPasswordsMatch(errors, user);
    }

    protected void checkPasswordsMatch(Errors errors, User user) {
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }

    protected void checkValidString(Errors errors, String field, String value, int minLength, int maxLength, String s) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "NotEmpty");
        if (value.length() < minLength || value.length() > maxLength) {
            errors.rejectValue(field, s);
        }
    }
}
