package com.hevelian.identity.entitlement.api.dto;

import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishToPDPRequestDTO {
  @NotNull
  @Size(min = 1)
  private Set<String> policyIds;
  private Boolean enabled;
  @Min(0)
  private Integer order;
}
