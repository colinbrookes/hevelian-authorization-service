package com.hevelian.identity.users.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleNameRequestDTO extends RoleRequestDTO {
    @NotBlank
    private String newName;
}
