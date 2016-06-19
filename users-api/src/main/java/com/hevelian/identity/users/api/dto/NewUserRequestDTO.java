package com.hevelian.identity.users.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;

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
        user.setRoles(getRoles().stream().map(n -> {
            Role r = new Role();
            r.setName(n);
            return r;
        }).collect(Collectors.toSet()));
        return user;
    }

}
