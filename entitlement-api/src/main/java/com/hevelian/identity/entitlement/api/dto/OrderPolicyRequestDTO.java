package com.hevelian.identity.entitlement.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPolicyRequestDTO extends PolicyIdDTO {
  @NotNull
  @Min(0)
  private Integer order;
}
