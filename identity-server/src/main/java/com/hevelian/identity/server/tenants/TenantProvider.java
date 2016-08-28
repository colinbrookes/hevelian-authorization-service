package com.hevelian.identity.server.tenants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hevelian.identity.core.ITenantProvider;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantProvider implements ITenantProvider {
    private final TenantRepository tenantRepository;

    public Tenant getCurrentTenant() {
        return tenantRepository.findOne(TenantUtil.getCurrentTenantId());
    };
}
