package com.example.javaproject.services;

import com.example.javaproject.data.RoleRepository;
import com.example.javaproject.models.Role;
import com.example.javaproject.models.RoleName;
import com.example.javaproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateHelperImpl implements TemplateHelper {

    private final SecurityService securityService;
    private final Role adminRole;

    @Autowired
    public TemplateHelperImpl(SecurityService securityService, RoleRepository roleRepository) {
        this.securityService = securityService;
        adminRole = roleRepository.findByName(RoleName.ADMIN.name);
    }

    @Override
    public boolean isLoggedIn() {
        return securityService.findLoggedInUsername() != null;
    }

    @Override
    public boolean isAdmin() {
        return securityService.findLoggedInUser().getRoles().contains(adminRole);
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().contains(adminRole);
    }

    @Override
    public String getUsername() {
        return securityService.findLoggedInUsername();
    }
}
