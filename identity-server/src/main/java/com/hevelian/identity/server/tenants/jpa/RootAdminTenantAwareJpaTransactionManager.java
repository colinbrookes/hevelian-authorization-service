package com.hevelian.identity.server.tenants.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RootAdminTenantAwareJpaTransactionManager extends TenantAwareJpaTransactionManager {
    private static final long serialVersionUID = 672871952950714288L;

    @Override
    protected void setProperties(final EntityManager em) {
        final Serializable tenantId = getTenantResolver().getCurrentTenantId();
        if (tenantId != null) {
            em.setProperty(getMultitenantProperty(), tenantId);
        } else {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();

            if (user != null && isRootAdmin(user)) {
                // TODO fix the message
                log.debug("Root administrator begins a new transaction. No tenant id is detected.");
            } else {
                throw new NoTenantProvidedException();
            }
        }
    }

    // TODO Move to some utility class
    private boolean isRootAdmin(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            if ("ROLE_SUPER_ADMIN".equals(auth.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
