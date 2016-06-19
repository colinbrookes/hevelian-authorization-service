package com.hevelian.identity.core.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.TenantService.TenantNotFoundByDomainException;
import com.hevelian.identity.core.api.dto.TenantDomainDTO;
import com.hevelian.identity.core.api.dto.TenantRequestDTO;
import com.hevelian.identity.core.api.dto.UserRequestDTO.NewTenantGroup;
import com.hevelian.identity.core.model.Tenant;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/TenantService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class TenantController {
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/activateTenant", method = RequestMethod.POST)
    public void activateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
            throws TenantNotFoundByDomainException {
        tenantService.activateTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/deactivateTenant", method = RequestMethod.POST)
    public void deactivateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
            throws TenantNotFoundByDomainException {
        tenantService.deactivateTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/addTenant", method = RequestMethod.POST)
    public PrimitiveResult<String> addTenant(
            @Validated(value = NewTenantGroup.class) @RequestBody TenantRequestDTO tenant) {
        tenant.getTenantAdmin()
                .setPassword(passwordEncoder.encode(tenant.getTenantAdmin().getPassword()));
        return new PrimitiveResult<String>(tenantService.addTenant(tenant.toEntity()).getDomain());
    }

    @RequestMapping(path = "/updateTenant", method = RequestMethod.POST)
    public void updateTenant(@Valid @RequestBody TenantRequestDTO tenant)
            throws TenantNotFoundByDomainException {
        if (tenant.getTenantAdmin().getPassword() != null)
            tenant.getTenantAdmin()
                    .setPassword(passwordEncoder.encode(tenant.getTenantAdmin().getPassword()));
        tenantService.updateTenant(tenant.toEntity());
    }

    @RequestMapping(path = "/deleteTenant", method = RequestMethod.POST)
    public void deleteTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
            throws TenantNotFoundByDomainException {
        tenantService.deleteTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/getTenant", method = RequestMethod.POST)
    public Tenant getTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
            throws TenantNotFoundByDomainException {
        return tenantService.getTenant(tenantDomainDTO.getTenantDomain());
    }

    @RequestMapping(path = "/getAllTenants", method = RequestMethod.GET)
    public Tenant[] getAllTenants() {
        return Iterables.toArray(tenantService.getAllTenants(), Tenant.class);
    }
}
