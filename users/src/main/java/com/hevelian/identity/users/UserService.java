package com.hevelian.identity.users;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.hevelian.identity.core.ITenantProvider;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.core.exc.IllegalEntityStateException;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.RoleRepository;
import com.hevelian.identity.users.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ITenantProvider tenantProvider;

    @Transactional(readOnly = false)
    public Role addRole(Role role) {
        Preconditions.checkArgument(role.getId() == null);
        return roleRepository.save(role);
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

    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<User> getUsersOfRole(Role role) throws RoleNotFoundByNameException {
        Role r = role;
        if (role.getId() == null) {
            r = roleRepository.findOneByName(role.getName());
            if (r == null) {
                throw new RoleNotFoundByNameException(role.getName());
            }
        }
        return userRepository.findByRoles_Name(r.getName());
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

    public void deleteUser(String userName)
            throws UserNotFoundByNameException, UserNotDeletableException {
        Set<User> nonDeletableUsers = userRepository
                .findByNameIsInAndDeletable(Sets.newHashSet(userName), false);
        if (nonDeletableUsers.size() != 0) {
            User user = Iterables.getOnlyElement(nonDeletableUsers);
            // Additional check for more detailed exception
            Tenant tenant = tenantProvider.getCurrentTenant();
            if (tenant.getAdminName().equals(user.getName()))
                throw new TenantAdminNotDeletableException(user.getName(), tenant.getDomain());
            throw new UserNotDeletableException(
                    Iterables.getOnlyElement(nonDeletableUsers).getName());
        }
        // Safe to delete by name as all checks for deletable have been
        // performed.
        int affectedRows = userRepository.deleteByName(userName);
        if (affectedRows == 0) {
            throw new UserNotFoundByNameException(userName);
        }
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

    // TODO refactor this
    public void addRemoveUsersOfRole(String roleName, Set<String> newUserNames,
            Set<String> removedUserNames)
            throws RoleNotFoundByNameException, UsersNotFoundByNameException {
        Role role = roleRepository.findOneByName(roleName);
        if (role == null) {
            throw new RoleNotFoundByNameException(roleName);
        }

        Set<String> missingUserNames = new HashSet<>();
        // TODO find a way to do this using JPA
        removedUserNames.forEach(n -> {
            User u = userRepository.findOneByName(n);
            if (u == null) {
                missingUserNames.add(n);
            } else {
                u.getRoles().remove(role);
                // Save one by one instead of collectiong all to same collection
                // and saving all at once. Maybe we will get too many names, who
                // knows..
                userRepository.save(u);
            }
        });

        if (!missingUserNames.isEmpty()) {
            throw new UsersNotFoundByNameException(missingUserNames);
        }

        // TODO find a way to do this using JPA
        newUserNames.forEach(n -> {
            User u = userRepository.findOneByName(n);
            if (u == null) {
                missingUserNames.add(n);
            } else {
                u.getRoles().add(role);
                // Save one by one instead of collectiong all to same collection
                // and saving all at once. Maybe we will get too many names, who
                // knows..
                userRepository.save(u);
            }
        });

        if (!missingUserNames.isEmpty()) {
            throw new UsersNotFoundByNameException(missingUserNames);
        }

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

    public void updateUsersOfRole(String roleName, Set<String> newUserNames)
            throws UsersNotFoundByNameException, RoleNotFoundByNameException {
        Role role = roleRepository.findOneByName(roleName);
        if (role == null) {
            throw new RoleNotFoundByNameException(roleName);
        }
        Long roleId = role.getId();
        roleRepository.deleteAllUsers(roleId);
        Set<String> missingUserNames = new HashSet<>();
        // TODO find a way to assign multiple users to some role using query.
        // Something like this:
        // INSERT INTO USER_ROLE(USER_ID, ROLES_ID) SELECT ID, ?1 AS
        // ROLES_ID FROM USER WHERE USER.NAME IN (?2)
        newUserNames.forEach(n -> {
            User u = userRepository.findOneByName(n);
            if (u == null) {
                missingUserNames.add(n);
            } else {
                u.getRoles().add(role);
                // Save one by one instead of collectiong all to same collection
                // and saving all at once. Maybe we will get too many names, who
                // knows..
                userRepository.save(u);
            }
        });

        if (!missingUserNames.isEmpty()) {
            throw new UsersNotFoundByNameException(missingUserNames);
        }
    }

    public static class RoleNotFoundByNameException extends EntityNotFoundByCriteriaException {
        private static final long serialVersionUID = -8295998392759277017L;

        public RoleNotFoundByNameException(String value) {
            super("name", value);
        }
    }

    @Getter
    public static class UserNotDeletableException extends IllegalEntityStateException {
        private static final long serialVersionUID = 1184952922996631305L;

        private final String userName;

        public UserNotDeletableException(String userName) {
            super(String.format("User '%s' cannot be deleted.", userName));
            this.userName = userName;
        }
    }

    @Getter
    public static class TenantAdminNotDeletableException extends UserNotDeletableException {
        private static final long serialVersionUID = 2219708808753725933L;
        private final String tenant;

        public TenantAdminNotDeletableException(String userName, String tenant) {
            super(userName);
            this.tenant = tenant;
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

    public static class UsersNotFoundByNameException extends UserNotFoundByNameException {
        private static final long serialVersionUID = -5578641450581609271L;

        public UsersNotFoundByNameException(Set<String> userNames) {
            super(Joiner.on(", ").join(userNames));
        }
    }
}
