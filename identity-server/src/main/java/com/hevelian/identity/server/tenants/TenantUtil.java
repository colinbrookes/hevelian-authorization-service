package com.hevelian.identity.server.tenants;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TenantUtil {
  private final String TENANT_ID_ATTRIBUTE = "CURRENT_TENANT_ID";

  public void setCurrentTenantId(Long tenantId) {
    RequestContextHolder.currentRequestAttributes().setAttribute(TENANT_ID_ATTRIBUTE, tenantId,
        RequestAttributes.SCOPE_SESSION);
  }

  public Long getCurrentTenantId() {
    return (Long) RequestContextHolder.currentRequestAttributes().getAttribute(TENANT_ID_ATTRIBUTE,
        RequestAttributes.SCOPE_SESSION);
  }
}
