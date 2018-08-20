package com.hevelian.identity.entitlement.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class TryPAPPolicyRequestDTO {
  @NotBlank
  private String policyId;
  private String subject;
  private String resource;
  private String action;
  private String environment;
}
