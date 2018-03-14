package com.hevelian.identity.server.tenants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.config.EntityManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.TenantService.TenantLifecycleService;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.users.repository.RoleRepository;
import com.hevelian.identity.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.SUPER_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantLifecycleServiceImpl implements TenantLifecycleService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void tenantCreated(Tenant tenant) {
    // do nothing for now.
  }

  @Override
  public void tenantDeleted(Tenant tenant) {
    entityManager.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, tenant.getId());
    userRepository.deleteAll();
    roleRepository.deleteAll();
    entityManager.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, null);

  }

}
