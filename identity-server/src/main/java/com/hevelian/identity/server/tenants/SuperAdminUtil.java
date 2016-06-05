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

}
