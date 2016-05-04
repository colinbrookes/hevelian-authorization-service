package com.hevelian.identity.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.RoleRepository;
import com.hevelian.identity.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String userName) {
        return userRepository.findOne(userName);
    }

    public void deleteRole(String roleName) {
        roleRepository.deleteByName(roleName);

    }

    public void deleteUser(String userName) {
        userRepository.deleteByName(userName);
    }

    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

}
