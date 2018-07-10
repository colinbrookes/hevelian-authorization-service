package com.hevelian.identity.users.api;

import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.users.UserService;
import com.hevelian.identity.users.UserService.*;
import com.hevelian.identity.users.api.dto.*;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/UserService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @RequestMapping(path = "/addRemoveRolesOfUser", method = RequestMethod.POST)
  public void addRemoveRolesOfUser(
      @Valid @RequestBody AddRemoveUserRolesRequestDTO addRemoveUserRolesRequest)
      throws UserNotFoundByNameException, RolesNotFoundByNameException {
    userService.addRemoveRolesOfUser(addRemoveUserRolesRequest.getName(),
        addRemoveUserRolesRequest.getNewRoles(), addRemoveUserRolesRequest.getRemovedRoles());
  }

  @RequestMapping(path = "/addRemoveUsersOfRole", method = RequestMethod.POST)
  public void addRemoveUsersOfRole(
      @Valid @RequestBody AddRemoveUsersOfRoleRequestDTO addRemoveUsersOfRoleRequest)
      throws UsersNotFoundByNameException, RoleNotFoundByNameException {
    userService.addRemoveUsersOfRole(addRemoveUsersOfRoleRequest.getName(),
        addRemoveUsersOfRoleRequest.getNewUserNames(),
        addRemoveUsersOfRoleRequest.getRemovedUserNames());
  }

  @RequestMapping(path = "/addRole", method = RequestMethod.POST)
  public PrimitiveResult<String> addRole(@Valid @RequestBody RoleRequestDTO role) {
    return new PrimitiveResult<String>(userService.addRole(role.toEntity()).getName());
  }

  @RequestMapping(path = "/addUser", method = RequestMethod.POST)
  public PrimitiveResult<String> addUser(@Valid @RequestBody NewUserRequestDTO user)
      throws RolesNotFoundByNameException {
    User entity = user.toEntity();
    entity.setPassword(passwordEncoder.encode(user.getPassword()));
    return new PrimitiveResult<String>(userService.addUser(entity).getName());
  }

  @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
  public void changePassword(@Valid @RequestBody UserCredentialsRequestDTO userCredentials)
      throws UserNotFoundByNameException {
    userService.changePassword(userCredentials.getName(),
        passwordEncoder.encode(userCredentials.getPassword()));
  }

  @RequestMapping(path = "/deleteRole", method = RequestMethod.POST)
  public void deleteRole(@Valid @RequestBody RoleRequestDTO role)
      throws RoleNotFoundByNameException {
    userService.deleteRole(role.getName());
  }

  @RequestMapping(path = "/deleteUser", method = RequestMethod.POST)
  public void deleteUser(@Valid @RequestBody UserNameRequestDTO userName)
      throws UserNotFoundByNameException, UserNotDeletableException {
    userService.deleteUser(userName.getName());
  }

  @RequestMapping(path = "/listRoles", method = RequestMethod.GET)
  public Iterable<Role> listRoles() {
    return userService.findAllRoles();
  }

  @RequestMapping(path = "/listUsers", method = RequestMethod.GET)
  public Iterable<User> listUsers(@RequestParam(name = "start", defaultValue = "0", required = false) Integer start,
                                  @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
                                  @RequestParam(name = "sort", required = false) String sort,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "role", required = false) String role) {
    Sort.Direction DIRECTION = Sort.Direction.ASC;
    String sortAttribute = "name";
    if (sort != null && sort.contains(" ")) {
      String[] sortExpression = sort.split(" ");
      sortAttribute = sortExpression[0];
      DIRECTION = Sort.Direction.fromString(sortExpression[1]);
    }
    PageRequest request = new PageRequest(start, size, DIRECTION, sortAttribute);
    if (name != null || role != null) {
      return userService.findUsersByFilter(name, role, request);
    }
    return userService.getAllUsersPaginated(new PageRequest(start, size, DIRECTION, sortAttribute));
  }

  @RequestMapping(path = "/getRolesOfUser", method = RequestMethod.POST)
  public Iterable<Role> getRolesOfUser(@Valid @RequestBody UserNameRequestDTO userName)
      throws UserNotFoundByNameException {
    return userService.getUser(userName.getName()).getRoles();
  }

  @RequestMapping(path = "/getUsersOfRole", method = RequestMethod.POST)
  public Iterable<User> getUsersOfRole(@Valid @RequestBody RoleRequestDTO role)
      throws RoleNotFoundByNameException {
    return userService.getUsersOfRole(role.toEntity());
  }

  @RequestMapping(path = "/updateRoleName", method = RequestMethod.POST)
  public void updateRoleName(@Valid @RequestBody UpdateRoleNameRequestDTO updateRoleNameRequest)
      throws RoleNotFoundByNameException {
    userService.updateRoleName(updateRoleNameRequest.getName(), updateRoleNameRequest.getNewName());
  }

  @RequestMapping(path = "/updateRolesOfUser", method = RequestMethod.POST)
  public void updateRolesOfUser(
      @Valid @RequestBody UpdateUserRolesRequestDTO updateUserRolesRequest)
      throws RolesNotFoundByNameException, UserNotFoundByNameException {
    userService.updateRolesOfUser(updateUserRolesRequest.getName(),
        updateUserRolesRequest.getNewRoles());
  }

  @RequestMapping(path = "/updateUsersOfRole", method = RequestMethod.POST)
  public void updateUsersOfRole(
      @Valid @RequestBody UpdateUsersOfRoleRequestDTO updateUsersOfRoleRequest)
      throws UsersNotFoundByNameException, RoleNotFoundByNameException {
    userService.updateUsersOfRole(updateUsersOfRoleRequest.getName(),
        updateUsersOfRoleRequest.getNewUserNames());
  }
}
