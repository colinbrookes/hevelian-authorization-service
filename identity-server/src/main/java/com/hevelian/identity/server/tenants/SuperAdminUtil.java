package com.hevelian.identity.server.tenants;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hevelian.identity.core.SystemRoles;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SuperAdminUtil {

    public boolean isSuperAdmin(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            if (SystemRoles.SUPER_ADMIN.equals(auth.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    // TODO remove these methods in case we do not need the mechanism of
    // temporary tenant id for super admin
    // public void setCurrentTenantId(Long tenantId) {
    // RequestContextHolder.currentRequestAttributes().setAttribute(TENANT_ID_ATTRIBUTE,
    // tenantId,
    // RequestAttributes.SCOPE_REQUEST);
    // }
    //
    // public Long getCurrentTenantId() {
    // return (Long) RequestContextHolder.currentRequestAttributes()
    // .getAttribute(TENANT_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    // }

}
