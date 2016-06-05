package com.hevelian.identity.server.auth.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.repository.TenantRepository;
import com.hevelian.identity.server.auth.UsernameParser;
import com.hevelian.identity.server.tenants.TenantUtil;
import com.hevelian.identity.users.UserService;
import com.hevelian.identity.users.model.PrimaryUser;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.repository.PrimaryUserRepository;
import com.hevelian.identity.users.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UserAuthenticationProvider extends AuthenticationProviderBase {
    @Autowired
    private UsernameParser usernameParser;
    @Autowired
    private AuthTenantService authTenantService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected Authentication authenticate(String username, String password)
            throws AuthenticationException {
        log.debug("Attempting to authenticate user '0'.", username);

        String tenantDomain = usernameParser.getTenant(username);
        Tenant tenant = authTenantService.getTenant(tenantDomain);
        if (tenant == null) {
            log.debug("Tenant '{0}' not found for username '{1}'.", tenantDomain, username);
            throw new BadCredentialsException("Bad Credentials");
        }

        TenantUtil.setCurrentTenantId(tenant.getId());

        com.hevelian.identity.users.model.PrimaryUser user = authUserService
                .getUser(usernameParser.getUser(username));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            log.debug("User '{0}' not found or credentials invalid.", username);
            throw new BadCredentialsException("Bad Credentials");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        log.debug("Authentication of user completed successfully for username '0'.", username);

        User userDetails = new User(username, password, authorities);
        userDetails.eraseCredentials();
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    protected boolean supports(String username, String password) {
        return usernameParser.getTenant(username) != null;
    }
}

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
class AuthTenantService {
    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    public Tenant getTenant(String tenantDomain) {
        return tenantRepository.findByDomain(tenantDomain);
    }
}

@Service
class AuthUserService extends UserService {
    @Autowired
    public AuthUserService(PrimaryUserRepository userRepository, RoleRepository roleRepository) {
        super(userRepository, roleRepository);
    }

    // We need propagation REQUIRES_NEW to make persistence manager reload the
    // new tenant id.
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public PrimaryUser getUser(String userName) {
        return super.getUser(userName);
    }
}
