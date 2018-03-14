package com.hevelian.identity.users.api.dto;

import javax.validation.constraints.NotNull;
import com.hevelian.identity.core.api.dto.EntityDTO;
import com.hevelian.identity.users.model.Role;

public class RoleRequestDTO extends Role implements EntityDTO<Role> {
  @Override
  public Role toEntity() {
    Role role = new Role();
    role.setName(getName());
    return role;
  }

  @Override
  @NotNull
  // TODO add some pattern for role names
  public String getName() {
    return super.getName();
  };
}
