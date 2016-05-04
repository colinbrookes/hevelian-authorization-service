package com.hevelian.identity.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class TenantService {
    // TODO review the regexp
    public static final String DOMAIN_REGEXP = "\\w*\\.\\w*";
    private final TenantRepository tenantRepository;

    // TODO throw exception if number of affected rows is not 1
    public void activateTenant(String tenantDomain) {
        tenantRepository.setActive(tenantDomain, true);
    }

    // TODO throw exception if number of affected rows is not 1
    public void deactivateTenant(String tenantDomain) {
        tenantRepository.setActive(tenantDomain, false);
    }

    public String addTenant(Tenant tenant) {
        return tenantRepository.save(tenant).getDomain();
    }

    public void deleteTenant(String tenantDomain) {
        tenantRepository.deleteByDomain(tenantDomain);
    }

    public Tenant getTenant(String tenantDomain) {
        return tenantRepository.findByDomain(tenantDomain);
    }

    public Tenant updateTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    public Iterable<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }
}
