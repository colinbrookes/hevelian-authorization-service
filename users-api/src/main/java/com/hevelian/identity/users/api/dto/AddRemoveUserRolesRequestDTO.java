package com.hevelian.identity.users.api.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRemoveUserRolesRequestDTO extends UpdateUserRolesRequestDTO {
  @NotNull
  private Set<String> removedRoles;
}
