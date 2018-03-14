package com.hevelian.identity.users.api.dto;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.util.UserRoleUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRequestDTO extends UserRequestDTO {
  @NotNull
  @Valid
  private Set<String> roles;

  @Override
  public User toEntity() {
    User user = super.toEntity();
    user.setRoles(UserRoleUtil.namesToRoles(getRoles()));
    return user;
  }

}
