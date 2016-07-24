package com.hevelian.identity.core.api.dto;

import javax.validation.constraints.NotNull;

import com.hevelian.identity.core.userinfo.UserInfo;

import lombok.Setter;

@Setter
public class TenantAdminRequestDTO implements UserInfo, EntityDTO<UserInfo> {

    private String name;
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
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull(groups = NewTenantGroup.class)
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isDeletable() {
        return false;
    }

    public static interface NewTenantGroup {

    }
}
