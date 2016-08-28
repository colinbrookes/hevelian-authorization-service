package com.hevelian.identity.core.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hevelian.identity.core.model.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantAdminRequestDTO implements UserInfo, EntityDTO<UserInfo> {

    @NotBlank
    private String name;
    @NotBlank(groups = NewTenantGroup.class)
    // TODO password validator
    private String password;

    @Override
    public UserInfo toEntity() {
        UserInfo user = new UserInfo() {
            @Override
            public String getName() {
                return TenantAdminRequestDTO.this.getName();
            }

            @Override
            public String getPassword() {
                return TenantAdminRequestDTO.this.getPassword();
            }

            @Override
            public boolean isDeletable() {
                return TenantAdminRequestDTO.this.isDeletable();
            }

        };
        return user;
    }

    @Override
    @JsonIgnore
    public boolean isDeletable() {
        return false;
    }

    public static interface NewTenantGroup {

    }
}
