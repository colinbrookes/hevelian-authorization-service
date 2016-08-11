package com.hevelian.identity.core.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hevelian.identity.core.api.validation.constraints.Domain;
import com.hevelian.identity.core.model.Tenant;

import lombok.Getter;
import lombok.Setter;

public class TenantRequestDTO extends Tenant implements EntityDTO<Tenant> {
    @Getter
    @Setter
    @NotNull
    @Valid
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
    @Email
    public String getContactEmail() {
        return super.getContactEmail();
    }

    @Override
    @NotNull
    @Domain
    public String getDomain() {
        return super.getDomain();
    }

    @Override
    @JsonIgnore
    public String getAdminName() {
        return super.getAdminName();
    }
}
