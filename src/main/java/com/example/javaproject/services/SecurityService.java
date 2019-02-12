package com.example.javaproject.services;

import com.example.javaproject.models.User;

public interface SecurityService {
    String findLoggedInUsername();
    User findLoggedInUser();
    void autologin(String username, String password);
}
