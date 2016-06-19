package com.hevelian.identity.core.api.dto;

import javax.validation.constraints.NotNull;

import com.hevelian.identity.core.model.User;

public class UserRequestDTO extends User implements EntityDTO<User> {

    @Override
    public User toEntity() {
        User user = new User();
        user.setName(getName());
        user.setPassword(getPassword());
        return user;
    }

    @Override
    @NotNull
    public String getName() {
        return super.getName();
    }

    @Override
    @NotNull(groups = NewTenantGroup.class)
    public String getPassword() {
        return super.getPassword();
    }

    public static interface NewTenantGroup {

    }
}
