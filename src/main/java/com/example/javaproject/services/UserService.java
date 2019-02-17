package com.example.javaproject.services;

import com.example.javaproject.models.User;

import java.util.List;

public interface UserService {
    void save(User user);
    void update(User user);
    void delete(User user);
    User findById(Long id);
    User findByUsername(String username);
    List<User> getAll();
}
