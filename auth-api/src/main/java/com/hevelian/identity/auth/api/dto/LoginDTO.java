package com.hevelian.identity.auth.api.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private Boolean rememberMe;
}
