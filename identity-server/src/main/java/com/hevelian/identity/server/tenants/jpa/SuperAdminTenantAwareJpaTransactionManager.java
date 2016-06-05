package com.hevelian.identity.server.tenants.jpa;

import org.springframework.security.core.userdetails.UserDetails;

import com.hevelian.identity.server.tenants.SuperAdminUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class SuperAdminTenantAwareJpaTransactionManager
        extends AuthenticationTenantAwareJpaTransactionManager {
    private static final long serialVersionUID = 672871952950714288L;

    @Override
    protected void handleUser(UserDetails user) {
        if (!SuperAdminUtil.isSuperAdmin(user)) {
            throw new NoTenantProvidedException();
        }
        log.debug(
                "Super Administrator is requesting transaction. No tenant id provided to the context.");

    }
}
