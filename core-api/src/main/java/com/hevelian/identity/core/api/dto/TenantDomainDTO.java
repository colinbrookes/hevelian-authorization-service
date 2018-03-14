package com.hevelian.identity.core.api.dto;

import org.hibernate.validator.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantDomainDTO {
  @NotBlank
  private String tenantDomain;
}
