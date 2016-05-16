package com.hevelian.identity.server.tenants.resolvers;

import com.hevelian.identity.server.tenants.RootAdminUtil;

public class RootAdminTenantResolver implements CurrentTenantResolver<Long> {
    @Override
    public Long getCurrentTenantId() {
        return RootAdminUtil.getCurrentTenantId();
    }
}