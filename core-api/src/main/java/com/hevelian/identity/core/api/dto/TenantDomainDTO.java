package com.hevelian.identity.core.api.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantDomainDTO {
    @NotNull
    private String tenantDomain;
}
