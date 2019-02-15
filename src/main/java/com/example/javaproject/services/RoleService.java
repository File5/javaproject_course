package com.example.javaproject.services;

import com.example.javaproject.models.Role;
import com.example.javaproject.models.User;

public interface RoleService {
    Role findRoleByName(String name);
    void createRole(String name);
    void deleteRole(String name);
    void addRoleToUser(Role role, User user);
    void removeRoleFromUser(Role role, User user);
}
