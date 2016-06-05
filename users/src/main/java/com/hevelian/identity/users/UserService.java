package com.hevelian.identity.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.users.model.PrimaryUser;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.repository.PrimaryUserRepository;
import com.hevelian.identity.users.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class UserService {
    private final PrimaryUserRepository userRepository;
    private final RoleRepository roleRepository;

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public void addUser(PrimaryUser user) {
        userRepository.save(user);
    }

    public PrimaryUser getUser(String userName) {
        return userRepository.findOneByName(userName);
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

    public Iterable<PrimaryUser> findAllUsers() {
        return userRepository.findAll();
    }

}
