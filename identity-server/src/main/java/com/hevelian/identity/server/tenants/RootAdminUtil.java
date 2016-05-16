package com.hevelian.identity.server.tenants;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RootAdminUtil {
    private final String TENANT_ID_ATTRIBUTE = "CURRENT_TENANT_ID";

    public boolean isRootAdmin(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            // TODO Move role name to some constant
            if ("ROLE_SUPER_ADMIN".equals(auth.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentTenantId(Long tenantId) {
        RequestContextHolder.currentRequestAttributes().setAttribute(TENANT_ID_ATTRIBUTE, tenantId,
                RequestAttributes.SCOPE_REQUEST);
    }

    public Long getCurrentTenantId() {
        return (Long) RequestContextHolder.currentRequestAttributes()
                .getAttribute(TENANT_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    }
}
