package com.hevelian.identity.entitlement.model.pap;

import com.hevelian.identity.entitlement.model.PolicyType;

// Interface should be used as PAPPolicyHist cannot extend PAPPolicy
public interface IPAPPolicy {
  public String getContent();

  public String getPolicyId();

  public PolicyType getPolicyType();

  public Integer getVersion();
}
