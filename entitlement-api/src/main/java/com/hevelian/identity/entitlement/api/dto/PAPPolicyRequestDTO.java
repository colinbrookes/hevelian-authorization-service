package com.hevelian.identity.entitlement.api.dto;

import org.hibernate.validator.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PAPPolicyRequestDTO {
  @NotEmpty
  private String content;
}
