package com.hevelian.identity.users.api.dto;

import javax.validation.constraints.NotNull;

import com.hevelian.identity.core.api.dto.EntityDTO;
import com.hevelian.identity.core.model.UserCredentials;

public class UserCredentialsRequestDTO extends UserCredentials
        implements EntityDTO<UserCredentials> {
    @Override
    public UserCredentials toEntity() {
        UserCredentials creds = new UserCredentials();
        creds.setName(getName());
        creds.setPassword(getPassword());
        return creds;
    }

    @Override
    @NotNull
    public String getName() {
        return super.getName();
    }

    @Override
    @NotNull
    public String getPassword() {
        return super.getPassword();
    }

}
