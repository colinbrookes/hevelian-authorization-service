package com.hevelian.identity.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.repository.TenantRepository;
import com.hevelian.identity.core.userinfo.UserInfo;

import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.SUPER_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class TenantService {
    // TODO review the regexp
    public static final String DOMAIN_REGEXP = "\\w+\\.\\w+";
    private final TenantRepository tenantRepository;
    private final TenantAdminService tenantAdminService;

    @Transactional
    public void activateTenant(String tenantDomain) throws TenantNotFoundByDomainException {
        int affectedRows = tenantRepository.setActive(tenantDomain, true);
        if (affectedRows == 0) {
            throw new TenantNotFoundByDomainException(tenantDomain);
        }
    }

    @Transactional
    public void deactivateTenant(String tenantDomain) throws TenantNotFoundByDomainException {
        int affectedRows = tenantRepository.setActive(tenantDomain, false);
        if (affectedRows == 0) {
            throw new TenantNotFoundByDomainException(tenantDomain);
        }
    }

    @Transactional
    public Tenant addTenant(Tenant tenant, UserInfo tenantAdmin) {
        Preconditions.checkArgument(tenant.getId() == null);
        // TODO move this line to setTenantAdmin method
        Preconditions.checkArgument(Objects.equal(tenant.getAdminName(), tenantAdmin.getName()));
        tenantRepository.save(tenant);
        tenantAdminService.setTenantAdmin(tenant, tenantAdmin);
        return tenant;
    }

    @Transactional
    // TODO delete all the other tenant related info as well from the database
    public void deleteTenant(String tenantDomain) throws TenantNotFoundByDomainException {
        int affectedRows = tenantRepository.deleteByDomain(tenantDomain);
        if (affectedRows == 0) {
            throw new TenantNotFoundByDomainException(tenantDomain);
        }
    }

    public Tenant getTenant(String tenantDomain) throws TenantNotFoundByDomainException {
        Tenant tenant = tenantRepository.findByDomain(tenantDomain);
        if (tenant == null) {
            throw new TenantNotFoundByDomainException(tenantDomain);
        }
        return tenant;
    }

    @Transactional
    // TODO add admin update functionality
    public Tenant updateTenant(Tenant tenant, UserInfo tenantAdmin)
            throws TenantNotFoundByDomainException {
        Preconditions.checkArgument(tenant.getId() != null);
        Tenant tenantEntity = getTenant(tenant.getDomain());
        if (tenantEntity == null) {
            throw new TenantNotFoundByDomainException(tenant.getDomain());
        }
        Preconditions
                .checkArgument(Objects.equal(tenant.getAdminName(), tenantEntity.getAdminName()));
        tenantEntity.setActive(tenant.getActive());
        tenantEntity.setDescription(tenant.getDescription());
        tenantEntity.setContactEmail(tenant.getContactEmail());
        tenantRepository.save(tenantEntity);
        // TODO check in the following method whether the tenant admin name is
        // the same
        tenantAdminService.updateTenantAdmin(tenant, tenantAdmin);
        return tenantEntity;
    }

    public Iterable<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public static class TenantNotFoundByDomainException extends EntityNotFoundByCriteriaException {
        private static final long serialVersionUID = -3353395068580678695L;

        public TenantNotFoundByDomainException(String tenantDomain) {
            super("tenantDomain", tenantDomain);
        }
    }

    public static interface TenantAdminService {
        void setTenantAdmin(Tenant tenant, UserInfo tenantAdmin);

        void updateTenantAdmin(Tenant tenant, UserInfo tenantAdmin);
    }
}
