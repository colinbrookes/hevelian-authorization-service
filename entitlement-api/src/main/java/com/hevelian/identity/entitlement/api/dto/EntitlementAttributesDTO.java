package com.hevelian.identity.entitlement.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntitlementAttributesDTO {
  private String subject;
  private String resource;
  private String action;
  private String environment;

}
