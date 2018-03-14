package com.hevelian.identity.users.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNameRequestDTO {
  @NotNull
  private String name;
}
