package com.hevelian.identity.server.tenants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.util.Strings;
import org.eclipse.persistence.config.EntityManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.TenantService.TenantAdminService;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.userinfo.UserInfo;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.SUPER_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class TenantAdminServiceImpl implements TenantAdminService {

    private final UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void setTenantAdmin(Tenant tenant, UserInfo tenantAdmin) {
        Preconditions.checkArgument(tenant.getAdminName().equals(tenantAdmin.getName()));
        User user = new User();
        user.setEnabled(true);
        user.setName(tenantAdmin.getName());
        user.setPassword(tenantAdmin.getPassword());
        user.setDeletable(false);
        entityManager.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT,
                tenant.getId());
        userRepository.save(user);
        // TODO possibly to reset the Id (depending on approach)

    }

    @Override
    @Transactional
    // TODO reuse the method above
    public void updateTenantAdmin(Tenant tenant, UserInfo tenantAdmin) {
        Preconditions.checkArgument(tenant.getAdminName().equals(tenantAdmin.getName()));
        User user = new User();
        user.setEnabled(true);
        user.setName(tenantAdmin.getName());
        if (!Strings.isEmpty(tenantAdmin.getPassword())) {
            user.setPassword(tenantAdmin.getPassword());
        }
        user.setDeletable(false);
        entityManager.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT,
                tenant.getId());
        userRepository.save(user);
        // TODO possibly to reset the Id (depending on approach)
    }

}
