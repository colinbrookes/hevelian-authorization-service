package com.hevelian.identity.core.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.api.dto.TenantDomainDTO;
import com.hevelian.identity.core.api.dto.TenantRequestDTO;
import com.hevelian.identity.core.model.Tenant;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/TenantService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class TenantController {
    private final TenantService tenantService;

    @RequestMapping(path = "/activateTenant", method = RequestMethod.POST)
    public void activateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO) {
        tenantService.activateTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/deactivateTenant", method = RequestMethod.POST)
    public void deactivateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO) {
        tenantService.deactivateTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/addTenant", method = RequestMethod.POST)
    public PrimitiveResult<String> addTenant(@Valid @RequestBody TenantRequestDTO tenant) {
        return new PrimitiveResult<String>(tenantService.addTenant(tenant.toEntity()));
    }

    @RequestMapping(path = "/updateTenant", method = RequestMethod.POST)
    public Tenant updateTenant(@Valid @RequestBody TenantRequestDTO tenant) {
        return tenantService.updateTenant(tenant);
    }

    @RequestMapping(path = "/deleteTenant", method = RequestMethod.POST)
    public void deleteTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO) {
        tenantService.deleteTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/getTenant", method = RequestMethod.GET)
    public Tenant getTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO) {
        return tenantService.getTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/getAllTenants", method = RequestMethod.GET)
    public Tenant[] getAllTenants() {
        return Iterables.toArray(tenantService.getAllTenants(), Tenant.class);
    }
}
