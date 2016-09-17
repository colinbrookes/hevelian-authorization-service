package com.hevelian.identity.users.api.dto;

import javax.validation.constraints.NotNull;

import com.hevelian.identity.users.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO extends UserCredentialsRequestDTO {
    @NotNull
    private Boolean enabled;

    @Override
    public User toEntity() {
        User user = new User();
        user.setEnabled(getEnabled());
        user.setName(getName());
        user.setPassword(getPassword());
        user.setDeletable(true);
        return user;
    }

}
