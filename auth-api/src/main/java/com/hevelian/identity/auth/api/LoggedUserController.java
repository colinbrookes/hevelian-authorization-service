package com.hevelian.identity.auth.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hevelian.identity.core.exc.NotImplementedException;
import com.hevelian.identity.users.model.Role;

@RestController
@RequestMapping(path = "/LoggedUserService")
public class LoggedUserController {

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public void changePassword(String oldPassword, String password) {
        // TODO to implement. Passwords should be stored in a hashed form.
        throw new NotImplementedException();
    }

    @RequestMapping(path = "/getRoles", method = RequestMethod.GET)
    public Role[] getRoles() {
        throw new NotImplementedException();
    }
}
