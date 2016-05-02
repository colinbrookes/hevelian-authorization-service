package com.hevelian.identity.server.auth;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hevelian.identity.users.UserService;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;

@Component
public class DefaultAdminInitializer {

    // TODO read credentials from external config
    private String adminPassword = "admin";
    private String adminUsername = "admin";

    @Autowired
    private UserService userService;

    @PostConstruct
    private void init() {
        buildAdmin();
    }

    private void buildAdmin() {
        User admin = userService.getUser(adminUsername);

        if (admin == null) {
            admin = new User();
            admin.setName(adminUsername);
            admin.setPassword(adminPassword);
            admin.setEnabled(true);
            // TODO add admin role
            admin.setRoles(new HashSet<Role>());
        }
        userService.addUser(admin);
    }
}