package com.hevelian.identity.users.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.hevelian.identity.core.exc.NotImplementedException;
import com.hevelian.identity.users.UserService;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;

@RestController
@RequestMapping(path = "/UserService")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/addRemoveRolesOfUser", method = RequestMethod.POST)
    public void addRemoveRolesOfUser(String userName, String[] newRoles, String[] removedRoles) {
        // TODO implement.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/addRemoveUsersOfRole", method = RequestMethod.POST)
    public void addRemoveUsersOfRole(String roleName, String[] newUserNames,
            String[] removedUserNames) {
        // TODO implement.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/addRole", method = RequestMethod.POST)
    public void addRole(Role role) {
        userService.addRole(role);
    }

    @RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public void addRole(User user) {
        userService.addUser(user);
    }

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public void changePassword(String userName, String password) {
        // TODO to implement. Passwords should be stored in a hashed form.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/deleteRole", method = RequestMethod.POST)
    public void deleteRole(String roleName) {
        userService.deleteRole(roleName);
    }

    @RequestMapping(path = "/deleteUser", method = RequestMethod.POST)
    public void deleteUser(String userName) {
        userService.deleteUser(userName);
    }

    @RequestMapping(path = "/listRoles", method = RequestMethod.GET)
    public Role[] listRoles() {
        return Iterables.toArray(userService.findAllRoles(), Role.class);
    }

    @RequestMapping(path = "/listUsers", method = RequestMethod.GET)
    public User[] listUsers() {
        return Iterables.toArray(userService.findAllUsers(), User.class);
    }

    @RequestMapping(path = "/getRolesOfUser", method = RequestMethod.GET)
    public Role[] getRolesOfUser(String userName) {
        return Iterables.toArray(userService.getUser(userName).getRoles(), Role.class);
    }

    @RequestMapping(path = "/getUsersOfRole", method = RequestMethod.GET)
    public Role[] getUsersOfRole(String roleName) {
        // TODO to implement.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/updateRoleName", method = RequestMethod.POST)
    public Role[] updateRoleName(String roleName, String newRoleName) {
        // TODO to implement.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/updateRolesOfUser", method = RequestMethod.POST)
    public void updateRolesOfUser(String userName, String[] newRoles) {
        // TODO implement.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/updateUsersOfRole", method = RequestMethod.POST)
    public void updateUsersOfRole(String roleName, String[] newUserNames) {
        // TODO implement.
        throw new NotImplementedException();
    }
}
