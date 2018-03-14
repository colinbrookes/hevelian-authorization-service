package com.hevelian.identity.server.tenants.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class AuthenticationTenantAwareJpaTransactionManager
    extends TenantAwareJpaTransactionManager {
  private static final long serialVersionUID = 672871952950714288L;

  @Override
  protected void setProperties(final EntityManager em) {
    final Serializable tenantId = getTenantResolver().getCurrentTenantId();
    if (tenantId != null) {
      em.setProperty(getMultitenantProperty(), tenantId);
    } else {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null) {
        if (authentication.getPrincipal() != null) {
          handleUser((UserDetails) authentication.getPrincipal());
        } else {
          // User should belong to some tenant
          throw new NoTenantProvidedException();
        }
      } else {
        // User is not yet authenticated (request comes from the
        // authentication provider)
        log.debug(
            "Neither authentication object nor tenant id are available. No tenant id provided to the context.");
      }

    }
  }

  protected void handleUser(UserDetails user) {
    throw new NoTenantProvidedException();
  }
}
