package com.hevelian.identity.entitlement.model.pap;

import com.hevelian.identity.entitlement.model.PolicyType;

public interface IPAPPolicy {
  public String getContent();

  public String getPolicyId();

  public PolicyType getPolicyType();

  public Integer getVersion();
}
