package com.hevelian.identity.server.auth.providers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.server.auth.UsernameParser;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SuperAdminAuthenticationProvider extends AuthenticationProviderBase {
    @Autowired
    private UsernameParser usernameParser;

    @Override
    protected Authentication authenticate(String username, String password)
            throws AuthenticationException {
        log.debug("Attempting to authenticate Super Administrator by username '0'.", username);
        Authentication result = null;
        if ("admin".equals(username)
                // TODO encrypt password, move to some store
                && "admin".equals(password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority(SystemRoles.SUPER_ADMIN));
            User user = new User(username, password, grantedAuths);
            user.eraseCredentials();
            result = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                    user.getAuthorities());
            log.debug(
                    "Authentication of Super Administrator completed successfully for username '0'.",
                    username);
        } else {
            log.debug("User '{0}' not found or credentials invalid.", username);
            throw new BadCredentialsException("Bad Credentials");
        }
        return result;
    }

    @Override
    protected boolean supports(String username, String password) {
        return usernameParser.getTenant(username) == null;
    }
}