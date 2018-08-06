package com.hevelian.identity.core;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.hevelian.identity.core.exc.EntityAlreadyExistsException;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.core.exc.IllegalEntityStateException;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.model.UserInfo;
import com.hevelian.identity.core.repository.TenantRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Secured(value = SystemRoles.SUPER_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantService {
  /**
   * This regular expression pattern is able to match most of the "real-working" domain names. The
   * pattern makes sure domain name matches the following criteria :
   * <ul>
   * <li>The domain name should be a-z | A-Z | 0-9 and hyphen(-)</li>
   * <li>The domain name should be between 1 and 63 characters long</li>
   * <li>Last Tld must be at least two characters, and a maximum of 6 characters</li>
   * <li>The domain name should not start or end with hyphen (-) (e.g. -hevelian.com or
   * hevelian-.com)</li>
   * <li>The domain name can be a subdomain (e.g. identity.hevelian.com)</li>
   */
  // Source:
  // http://www.mkyong.com/regular-expressions/domain-name-regular-expression-example/
  public static final String DOMAIN_REGEXP = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
  private final TenantRepository tenantRepository;
  private final TenantAdminService tenantAdminService;
  private final TenantLifecycleService tenantLifecycleService;

  @Transactional
  public void activateTenant(String tenantDomain)
      throws TenantNotFoundByDomainException, TenantActiveAlreadyInStateException {
    Tenant tenant = getTenant(tenantDomain);
    if (tenant.getActive())
      throw new TenantActiveAlreadyInStateException(true);
    tenant.setActive(true);
    tenantRepository.save(tenant);
  }

  @Transactional
  public void deactivateTenant(String tenantDomain)
      throws TenantNotFoundByDomainException, TenantActiveAlreadyInStateException {
    Tenant tenant = getTenant(tenantDomain);
    if (!tenant.getActive())
      throw new TenantActiveAlreadyInStateException(false);
    tenant.setActive(false);
    tenantRepository.save(tenant);
  }

  @Transactional
  public Tenant addTenant(Tenant tenant, UserInfo tenantAdmin)
      throws TenantDomainAlreadyExistsException {
    String domain = tenant.getDomain();
    if (tenantRepository.findByDomain(domain) != null) {
      throw new TenantDomainAlreadyExistsException(domain);
    }
    tenantRepository.save(tenant);
    tenantAdminService.createTenantAdmin(tenant, tenantAdmin);
    tenantLifecycleService.tenantCreated(tenant);
    return tenant;
  }

  @Transactional
  public void deleteTenant(String tenantDomain) throws TenantNotFoundByDomainException {
    Tenant tenant = getTenant(tenantDomain);
    tenantRepository.delete(tenant);
    tenantLifecycleService.tenantDeleted(tenant);
  }

  public Tenant getTenant(String tenantDomain) throws TenantNotFoundByDomainException {
    Tenant tenant = tenantRepository.findByDomain(tenantDomain);
    if (tenant == null)
      throw new TenantNotFoundByDomainException(tenantDomain);
    return tenant;
  }

  @Transactional
  public Tenant updateTenant(Tenant tenant, UserInfo tenantAdmin)
      throws TenantNotFoundByDomainException {
    Preconditions.checkArgument(tenant.getId() == null);
    Tenant tenantEntity = getTenant(tenant.getDomain());

    // Delete previous tenant administrator in case its name changed.
    boolean newAdmin = false;
    if (!Objects.equal(tenantEntity.getAdminName(), tenantAdmin.getName())) {
      tenantAdminService.deleteTenantAdmin(tenantEntity);
      newAdmin = true;
    }

    tenantEntity.setActive(tenant.getActive());
    tenantEntity.setDescription(tenant.getDescription());
    tenantEntity.setContactEmail(tenant.getContactEmail());
    tenantEntity.setAdminName(tenant.getAdminName());
    tenantRepository.save(tenantEntity);

    if (newAdmin)
      tenantAdminService.createTenantAdmin(tenantEntity, tenantAdmin);
    else
      tenantAdminService.updateTenantAdmin(tenantEntity, tenantAdmin);

    return tenantEntity;
  }

  public Page<Tenant> searchTenants(Specification<Tenant> spec, PageRequest request) {
    return tenantRepository.findAll(spec, request);
  }

  @Getter
  public static class TenantActiveAlreadyInStateException extends IllegalEntityStateException {
    private static final long serialVersionUID = -622542404766092404L;
    private final boolean active;

    public TenantActiveAlreadyInStateException(boolean active) {
      super(String.format(
          "The tenant active state cannot be set to '%s' because it is already the current state.",
          active));
      this.active = active;
    }
  }

  public static class TenantNotFoundByDomainException extends EntityNotFoundByCriteriaException {
    private static final long serialVersionUID = -3353395068580678695L;

    public TenantNotFoundByDomainException(String tenantDomain) {
      super("tenantDomain", tenantDomain);
    }
  }

  @Getter
  public static class TenantDomainAlreadyExistsException extends EntityAlreadyExistsException {
    private final String domain;

    public TenantDomainAlreadyExistsException(String domain) {
      super(domain);
      this.domain = domain;
    }
  }

  public static interface TenantAdminService {
    void createTenantAdmin(Tenant tenant, UserInfo tenantAdmin);

    void updateTenantAdmin(Tenant tenant, UserInfo tenantAdmin);

    void deleteTenantAdmin(Tenant tenant);
  }

  public static interface TenantLifecycleService {
    void tenantCreated(Tenant tenant);

    void tenantDeleted(Tenant tenant);
  }
}
