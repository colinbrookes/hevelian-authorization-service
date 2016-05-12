package com.hevelian.identity.server.tenants.resolvers;

import java.io.Serializable;

public interface CurrentTenantResolver<T extends Serializable> {
    T getCurrentTenantId();
}