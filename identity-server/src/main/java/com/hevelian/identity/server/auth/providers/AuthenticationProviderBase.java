package com.hevelian.identity.server.auth.providers;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public abstract class AuthenticationProviderBase implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Authentication result = null;
        if (supports(username, password)) {
            result = authenticate(username, password);
        }
        return result;
    }

    protected abstract Authentication authenticate(String username, String password)
            throws AuthenticationException;

    protected abstract boolean supports(String username, String password);

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
