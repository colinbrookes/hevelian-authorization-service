package com.hevelian.identity.entitlement.exc;

import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;

public class PolicyNotFoundByPolicyIdException extends EntityNotFoundByCriteriaException {
  private static final long serialVersionUID = 8894276524310804961L;

  public PolicyNotFoundByPolicyIdException(String policyId) {
    super("policyId", policyId);
  }
}
