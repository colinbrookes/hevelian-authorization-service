package com.hevelian.identity.users;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.hevelian.identity.core.ITenantProvider;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.exc.EntityAlreadyExistException;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.core.exc.IllegalEntityStateException;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.RoleRepository;
import com.hevelian.identity.users.repository.UserRepository;
import com.hevelian.identity.users.util.UserRoleUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ITenantProvider tenantProvider;
  private final MultiUsersRoleAssignManager murAssignManager;

  @Transactional(readOnly = false)
  public Role addRole(Role role) throws RoleAlreadyExistException {
    String roleName = role.getName();
    if (roleRepository.findOneByName(roleName) != null)
      throw new RoleAlreadyExistException(roleName);
    return roleRepository.save(role);
  }

  @Transactional(readOnly = false)
  public User addUser(User user) throws RolesNotFoundByNameException, UserAlreadyExistException {
    String userName = user.getName();
    if (userRepository.findOneByName(userName) != null)
      throw new UserAlreadyExistException(userName);
    Set<String> userRoleNames = UserRoleUtil.rolesToNames(user.getRoles());
    Preconditions.checkArgument(userRoleNames.size() == user.getRoles().size());

    Set<Role> existingRoles = roleRepository.findByNameIsIn(userRoleNames);
    if (existingRoles.size() != user.getRoles().size()) {
      throw new RolesNotFoundByNameException(
          Sets.difference(userRoleNames, UserRoleUtil.rolesToNames(existingRoles)));
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

  public Page<Role> searchRoles(Specification<Role> spec, PageRequest request) {
    return roleRepository.findAll(spec, request);
  }

  public Page<User> searchUsers(Specification<User> spec, PageRequest request) {
    return userRepository.findAll(spec, request);
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
    Set<User> nonDeletableUsers =
        userRepository.findByNameIsInAndDeletable(Sets.newHashSet(userName), false);
    if (nonDeletableUsers.size() != 0) {
      User user = Iterables.getOnlyElement(nonDeletableUsers);
      // Additional check for more detailed exception
      Tenant tenant = tenantProvider.getCurrentTenant();
      if (tenant.getAdminName().equals(user.getName()))
        throw new TenantAdminNotDeletableException(user.getName(), tenant.getDomain());
      throw new UserNotDeletableException(Iterables.getOnlyElement(nonDeletableUsers).getName());
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
        throw new RolesNotFoundByNameException(
            Sets.difference(removedRoleNames, UserRoleUtil.rolesToNames(user.getRoles())));
      }
    }

    Set<Role> newRoles = roleRepository.findByNameIsIn(newRoleNames);
    if (newRoleNames.size() != newRoles.size()) {
      throw new RolesNotFoundByNameException(
          UserRoleUtil.getMissingRoleNames(newRoleNames, newRoles));
    }
    user.getRoles().addAll(newRoles);
    userRepository.save(user);
  }

  public void addRemoveUsersOfRole(String roleName, Set<String> newUserNames,
                                   Set<String> removedUserNames)
      throws RoleNotFoundByNameException, UsersNotFoundByNameException {
    Role role = roleRepository.findOneByName(roleName);
    if (role == null) {
      throw new RoleNotFoundByNameException(roleName);
    }

    murAssignManager.unassignRoleFromUsers(removedUserNames, role);
    murAssignManager.assignRoleToUsers(newUserNames, role);
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
      throw new RolesNotFoundByNameException(
          UserRoleUtil.getMissingRoleNames(newRoleNames, newRoles));
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
    roleRepository.deleteAllUsers(role.getId());
    murAssignManager.assignRoleToUsers(newUserNames, role);
  }

  public Role findRole(String name) {
    return roleRepository.findOneByName(name);
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

  @Getter
  public static class RoleAlreadyExistException extends EntityAlreadyExistException {
    private String name;

    public RoleAlreadyExistException(String roleName) {
      super(roleName);
      this.name = roleName;
    }
  }

  @Getter
  public static class UserAlreadyExistException extends EntityAlreadyExistException {
    private String name;

    public UserAlreadyExistException(String userName) {
      super(userName);
      this.name = userName;
    }
  }


  public static class UsersNotFoundByNameException extends UserNotFoundByNameException {
    private static final long serialVersionUID = -5578641450581609271L;

    public UsersNotFoundByNameException(Set<String> userNames) {
      super(Joiner.on(", ").join(userNames));
    }
  }
}
