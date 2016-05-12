package com.hevelian.identity.core.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.model.Tenant;

public class TenantRequestDTO extends Tenant implements EntityDTO<Tenant> {

    @Override
    public Tenant toEntity() {
        Tenant entity = new Tenant();
        entity.setActive(getActive());
        entity.setDomain(getDomain());
        return entity;
    }

    @Override
    @NotNull
    public Boolean getActive() {
        return super.getActive();
    }

    @Override
    @NotNull
    // TODO localize message
    @Pattern(regexp = TenantService.DOMAIN_REGEXP, message = "Illegal characters in tenant domain. Letters, numbers, '.', '-' and '_' are allowed")
    public String getDomain() {
        return super.getDomain();
    }
}
