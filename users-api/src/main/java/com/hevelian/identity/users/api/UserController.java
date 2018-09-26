package com.hevelian.identity.users.api;

import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.core.api.pagination.PageRequestParameters;
import com.hevelian.identity.core.api.pagination.PageRequestParametersReader;
import com.hevelian.identity.core.pagination.PageRequestBuilder;
import com.hevelian.identity.core.specification.EntitySpecificationsBuilder;
import com.hevelian.identity.users.UserService;
import com.hevelian.identity.users.UserService.*;
import com.hevelian.identity.users.api.dto.*;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/UserService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
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
  public PrimitiveResult<String> addRole(@Valid @RequestBody RoleRequestDTO role) throws RoleAlreadyExistsException {
    return new PrimitiveResult<>(userService.addRole(role.toEntity()).getName());
  }

  @RequestMapping(path = "/addUser", method = RequestMethod.POST)
  public PrimitiveResult<String> addUser(@Valid @RequestBody NewUserRequestDTO user)
      throws RolesNotFoundByNameException, UserAlreadyExistsException {
    User entity = user.toEntity();
    entity.setPassword(passwordEncoder.encode(user.getPassword()));
    return new PrimitiveResult<>(userService.addUser(entity).getName());
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
  public Page<Role> listRoles(@ApiParam(value = PageRequestParameters.PAGE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_PAGE) @RequestParam(name = PageRequestParameters.PAGE, required = false) @Min(PageRequestParameters.PAGE_MIN) Integer page,
                              @ApiParam(value = PageRequestParameters.SIZE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_SIZE) @RequestParam(name = PageRequestParameters.SIZE, required = false) @Min(PageRequestParameters.SIZE_MIN) Integer size,
                              @ApiParam(value = PageRequestParameters.SORT_DESCRIPTION) @RequestParam(name = PageRequestParameters.SORT, required = false) String sort,
                              @ApiParam(value = "Role name") @RequestParam(required = false) String name) {
    PageRequestBuilder pageRequestBuilder = new PageRequestParametersReader().readParameters(page, size, sort);
    EntitySpecificationsBuilder<Role> builder = new EntitySpecificationsBuilder<>();
    builder.with(Role.FIELD_NAME, name);
    return userService.searchRoles(builder.build(), pageRequestBuilder.build());
  }

  @RequestMapping(path = "/listUsers", method = RequestMethod.GET)
  public Page<User> listUsers(@ApiParam(value = PageRequestParameters.PAGE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_PAGE) @RequestParam(name = PageRequestParameters.PAGE, required = false) @Min(PageRequestParameters.PAGE_MIN) Integer page,
                              @ApiParam(value = PageRequestParameters.SIZE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_SIZE) @RequestParam(name = PageRequestParameters.SIZE, required = false) @Min(PageRequestParameters.SIZE_MIN) Integer size,
                              @ApiParam(value = PageRequestParameters.SORT_DESCRIPTION) @RequestParam(name = PageRequestParameters.SORT, required = false) String sort,
                              @ApiParam(value = "User name") @RequestParam(required = false) String name,
                              @ApiParam(value = "User is enabled") @RequestParam(required = false) Boolean enabled) {
    PageRequestBuilder pageRequestBuilder = new PageRequestParametersReader().readParameters(page, size, sort);
    EntitySpecificationsBuilder<User> builder = new EntitySpecificationsBuilder<>();
    builder.with(User.FIELD_NAME, name);
    builder.with(User.FIELD_ENABLED, enabled);
    return userService.searchUsers(builder.build(), pageRequestBuilder.build());
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
