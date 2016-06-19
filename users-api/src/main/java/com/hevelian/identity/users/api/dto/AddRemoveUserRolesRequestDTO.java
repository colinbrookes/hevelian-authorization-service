package com.hevelian.identity.users.api.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRemoveUserRolesRequestDTO extends UserNameRequestDTO {
    private Set<String> newRoles;
    private Set<String> removedRoles;
}
