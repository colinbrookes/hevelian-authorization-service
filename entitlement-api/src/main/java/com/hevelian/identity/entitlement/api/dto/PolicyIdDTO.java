package com.hevelian.identity.entitlement.api.dto;

import org.hibernate.validator.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyIdDTO {
  @NotBlank
  private String policyId;
}
