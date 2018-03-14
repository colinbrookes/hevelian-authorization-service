package com.hevelian.identity.entitlement.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntitlementRequestDTO {
  @NotNull
  private String request;
}
