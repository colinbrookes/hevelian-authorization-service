package com.hevelian.identity.core.api.dao;

import javax.validation.constraints.NotNull;

public class TenantDomainDTO {
    @NotNull
    private String tenantDomain;

    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }
}
