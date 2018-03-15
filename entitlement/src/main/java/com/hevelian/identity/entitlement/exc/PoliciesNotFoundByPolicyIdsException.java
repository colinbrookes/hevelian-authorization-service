package com.hevelian.identity.entitlement.exc;

import java.util.Set;
import com.google.common.base.Joiner;

public class PoliciesNotFoundByPolicyIdsException extends PolicyNotFoundByPolicyIdException {
  private static final long serialVersionUID = 710880252040581498L;

  public PoliciesNotFoundByPolicyIdsException(Set<String> policyIds) {
    super(Joiner.on(", ").join(policyIds));
  }
}
