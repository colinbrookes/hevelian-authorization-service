package com.hevelian.identity.server.tenants.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.hevelian.identity.server.tenants.RootAdminUtil;
import com.hevelian.identity.server.tenants.resolvers.CurrentTenantResolver;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class RootAdminTenantAwareJpaTransactionManager extends TenantAwareJpaTransactionManager {
    private static final long serialVersionUID = 672871952950714288L;

    @NonNull
    private CurrentTenantResolver<? extends Serializable> rootAdminTenantResolver;

    @Override
    protected void setProperties(final EntityManager em) {
        final Serializable tenantId = getTenantResolver().getCurrentTenantId();
        if (tenantId != null) {
            em.setProperty(getMultitenantProperty(), tenantId);
        } else {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();

            if (user != null && RootAdminUtil.isRootAdmin(user)) {
                setTenantIdPropertyForRootAdmin(em);
            } else {
                throw new NoTenantProvidedException();
            }
        }
    }

    private void setTenantIdPropertyForRootAdmin(final EntityManager em) {
        final Serializable rootAdminTenantId = getRootAdminTenantResolver().getCurrentTenantId();
        if (rootAdminTenantId != null) {
            em.setProperty(getMultitenantProperty(), rootAdminTenantId);
        } else {
            // TODO fix the message
            log.debug("Root administrator begins a new transaction. No tenant id is detected.");
        }
    }

}
