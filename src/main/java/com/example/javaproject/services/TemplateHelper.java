package com.example.javaproject.services;

import com.example.javaproject.models.User;

public interface TemplateHelper {
    boolean isLoggedIn();
    boolean isAdmin();
    boolean isAdmin(User user);
    String getUsername();
}
