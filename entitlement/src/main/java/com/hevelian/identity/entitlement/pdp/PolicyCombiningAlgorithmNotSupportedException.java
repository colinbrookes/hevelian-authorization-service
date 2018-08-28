package com.hevelian.identity.entitlement.pdp;

import lombok.Getter;

public class PolicyCombiningAlgorithmNotSupportedException
    extends Exception {
  private static final long serialVersionUID = 6842798853008017883L;
  @Getter
  private final String policyCombiningAlgorithmId;

  public PolicyCombiningAlgorithmNotSupportedException(String policyCombiningAlgorithmId) {
    super(String.format("Policy Combining Algorithm '%s' not supported.", policyCombiningAlgorithmId));
    this.policyCombiningAlgorithmId = policyCombiningAlgorithmId;
  }
}