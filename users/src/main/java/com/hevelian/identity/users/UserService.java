package com.hevelian.identity.users;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.core.exc.NotImplementedException;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.RoleRepository;
import com.hevelian.identity.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = false)
    public Role addRole(Role role) {
        Preconditions.checkArgument(role.getId() == null);
        return roleRepository.save(role);
    }

    @Transactional(readOnly = false)
    public void addRemoveRolesOfUser(String userName, Set<String> newRoleNames,
            Set<String> removedRoleNames)
                    throws UserNotFoundByNameException, RolesNotFoundByNameException {
        User user = userRepository.findOneByName(userName);
        if (user == null) {
            throw new UserNotFoundByNameException(userName);
        }
        for (String n : removedRoleNames) {
            Role r = new Role();
            r.setName(n);
            if (!user.getRoles().remove(r)) {
                Set<String> diff = Sets.difference(removedRoleNames, user.getRoles().stream()
                        .map(r2 -> r2.getName()).collect(Collectors.toSet()));
                // TODO throw more specific exception
                throw new RolesNotFoundByNameException(diff);
            }
        }

        Set<Role> newRoles = roleRepository.findByNameIsIn(newRoleNames);
        if (newRoleNames.size() != newRoles.size()) {
            throw new RolesNotFoundByNameException(Sets.difference(newRoleNames,
                    newRoles.stream().map(r -> r.getName()).collect(Collectors.toSet())));
        }
        user.getRoles().addAll(newRoles);
        userRepository.save(user);
    }

    public void addRemoveUsersOfRole(String name, Set<String> newUserNames,
            Set<String> removedUserNames) {
        // TODO to implement.
        throw new NotImplementedException();
    }

    @Transactional(readOnly = false)
    public User addUser(User user) throws RolesNotFoundByNameException {
        Preconditions.checkArgument(user.getId() == null);
        Set<String> userRoleNames = user.getRoles().stream().map(r -> r.getName())
                .collect(Collectors.toSet());
        Preconditions.checkArgument(userRoleNames.size() == user.getRoles().size());

        Set<Role> existingRoles = roleRepository.findByNameIsIn(userRoleNames);
        if (existingRoles.size() != user.getRoles().size()) {
            Set<String> existingRoleNames = existingRoles.stream().map(r -> r.getName())
                    .collect(Collectors.toSet());
            throw new RolesNotFoundByNameException(
                    Sets.difference(userRoleNames, existingRoleNames));
        }
        user.getRoles().clear();
        user.getRoles().addAll(existingRoles);
        return userRepository.save(user);
    }

    public User getUser(String userName) throws UserNotFoundByNameException {
        User user = userRepository.findOneByName(userName);
        if (user == null) {
            throw new UserNotFoundByNameException(userName);
        }
        return user;
    }

    public void changePassword(String userName, String newPassword)
            throws UserNotFoundByNameException {
        int affectedRows = userRepository.changePassword(userName, newPassword);
        if (affectedRows == 0) {
            throw new UserNotFoundByNameException(userName);
        }
    }

    public void deleteRole(String roleName) throws RoleNotFoundByNameException {
        int affectedRows = roleRepository.deleteByName(roleName);
        if (affectedRows == 0) {
            throw new RoleNotFoundByNameException(roleName);
        }
    }

    public void deleteUser(String userName) throws UserNotFoundByNameException {
        int affectedRows = userRepository.deleteByName(userName);
        if (affectedRows == 0) {
            throw new UserNotFoundByNameException(userName);
        }
    }

    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    // TODO throw an exception if role does not exist
    public Iterable<User> getUsersOfRole(Role role) {
        return userRepository.findByRoles_Name(role.getName());
    }

    public void updateRoleName(String roleName, String newRoleName)
            throws RoleNotFoundByNameException {
        int affectedRows = roleRepository.updateName(roleName, newRoleName);
        if (affectedRows == 0) {
            throw new RoleNotFoundByNameException(roleName);
        }
    }

    public void updateRolesOfUser(String userName, Set<String> newRoleNames)
            throws RolesNotFoundByNameException, UserNotFoundByNameException {
        User user = userRepository.findOneByName(userName);
        if (user == null) {
            throw new UserNotFoundByNameException(userName);
        }

        Set<Role> newRoles = roleRepository.findByNameIsIn(newRoleNames);
        if (newRoleNames.size() != newRoles.size()) {
            throw new RolesNotFoundByNameException(Sets.difference(newRoleNames,
                    newRoles.stream().map(r -> r.getName()).collect(Collectors.toSet())));
        }
        user.getRoles().clear();
        user.getRoles().addAll(newRoles);
        userRepository.save(user);
    }

    public static class RoleNotFoundByNameException extends EntityNotFoundByCriteriaException {
        private static final long serialVersionUID = -8295998392759277017L;

        public RoleNotFoundByNameException(String value) {
            super("name", value);
        }
    }

    public static class RolesNotFoundByNameException extends RoleNotFoundByNameException {
        private static final long serialVersionUID = -4280994180946111524L;

        public RolesNotFoundByNameException(Set<String> roleNames) {
            super(Joiner.on(", ").join(roleNames));
        }
    }

    public static class UserNotFoundByNameException extends EntityNotFoundByCriteriaException {
        private static final long serialVersionUID = 525082643662104801L;

        public UserNotFoundByNameException(String userName) {
            super("name", userName);
        }
    }
}
