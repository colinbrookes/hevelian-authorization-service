package com.hevelian.identity.server.tenants.resolvers;

import com.hevelian.identity.server.tenants.TenantUtil;

public class SessionTenantResolver implements CurrentTenantResolver<Long> {
    @Override
    public Long getCurrentTenantId() {
        return TenantUtil.getCurrentTenantId();
    }
}