package com.hevelian.identity.core.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.model.Tenant;

import lombok.Setter;

public class TenantRequestDTO extends Tenant implements EntityDTO<Tenant> {
    @Setter
    private TenantAdminRequestDTO tenantAdmin;

    @Override
    public Tenant toEntity() {
        Tenant entity = new Tenant();
        entity.setActive(getActive());
        entity.setDomain(getDomain());
        entity.setContactEmail(getContactEmail());
        entity.setDescription(getDescription());
        entity.setAdminName(getTenantAdmin().getName());
        return entity;
    }

    @Override
    @NotNull
    public Boolean getActive() {
        return super.getActive();
    }

    @Override
    @NotNull
    // TODO set email pattern
    public String getContactEmail() {
        return super.getContactEmail();
    }

    @Override
    @NotNull
    // TODO localize message
    @Pattern(regexp = TenantService.DOMAIN_REGEXP, message = "Illegal characters in tenant domain. Letters, numbers, '.' and '_' are allowed. Example: tenant1.com")
    public String getDomain() {
        return super.getDomain();
    }

    @NotNull
    @Valid
    // TODO return UserInfo directly?
    public TenantAdminRequestDTO getTenantAdmin() {
        return tenantAdmin;
    }
}
