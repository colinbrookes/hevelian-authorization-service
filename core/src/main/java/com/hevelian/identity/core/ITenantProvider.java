package com.hevelian.identity.core;

import com.hevelian.identity.core.model.Tenant;

public interface ITenantProvider {
    Tenant getCurrentTenant();
}
