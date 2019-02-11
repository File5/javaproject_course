package com.example.javaproject.services;

import com.example.javaproject.models.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
