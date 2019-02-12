package com.example.javaproject.services;

import com.example.javaproject.data.RoleRepository;
import com.example.javaproject.data.UserRepository;
import com.example.javaproject.models.Role;
import com.example.javaproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void createRole(String name) {
        if (roleRepository.findByName(name) == null) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }

    @Override
    public void deleteRole(String name) {
        Role role = roleRepository.findByName(name);
        if (role != null) {
            roleRepository.delete(role);
        }
    }

    @Override
    public void addRoleToUser(Role role, User user) {
        user.getRoles().add(role);
    }

    @Override
    public void removeRoleFromUser(Role role, User user) {
        user.getRoles().remove(role);
    }
}
