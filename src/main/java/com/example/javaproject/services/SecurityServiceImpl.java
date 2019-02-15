package com.example.javaproject.services;

import com.example.javaproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService{

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public SecurityServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    public String findLoggedInUsername() {
        User user = findLoggedInUser();
        if (user != null) {
            return user.getUsername();
        }

        return null;
    }

    @Override
    public User findLoggedInUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) user).getUsername();
            return userService.findByUsername(username);
        }

        return null;
    }

    @Override
    public void autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        if (!usernamePasswordAuthenticationToken.isAuthenticated()) {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }

        if (usernamePasswordAuthenticationToken.isAuthenticated()) { // if authenticated successfully - update context
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}