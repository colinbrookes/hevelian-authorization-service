package com.hevelian.identity.auth.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hevelian.identity.auth.api.dto.LoginDTO;
import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.core.exc.NotImplementedException;

@RestController
@RequestMapping(path = "/LoginService")
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public PrimitiveResult<Boolean> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));
        boolean isAuthenticated = isAuthenticated(authentication);

        // TODO should I really check for isAuthenticated?
        if (isAuthenticated) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // TODO do the remember-me magic
        }
        return new PrimitiveResult<Boolean>(isAuthenticated);
    }

    // TODO move in service layer if it is really needed.
    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    @RequestMapping(path = "/loginWithRememberMeCookie", method = RequestMethod.POST)
    public void login(String cookie) {
        // TODO to implement. Passwords should be stored in a hashed form.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
