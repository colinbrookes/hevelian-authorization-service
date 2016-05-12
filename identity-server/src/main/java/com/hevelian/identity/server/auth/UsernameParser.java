package com.hevelian.identity.server.auth;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

@Component
public class UsernameParser {
    private static final String DOMAIN_SEPARATOR = "/";
    private static final String TENANT_SEPARATOR = "@";

    public String getDomain(String username) {
        List<String> splitToList = Splitter.on(DOMAIN_SEPARATOR).splitToList(username);
        int listSize = splitToList.size();
        return listSize < 2 ? null : splitToList.get(0);
    }

    public String getTenant(String username) {
        List<String> splitToList = Splitter.on(TENANT_SEPARATOR).splitToList(username);
        int listSize = splitToList.size();
        return listSize < 2 ? null : splitToList.get(listSize - 1);
    }

    public String getUser(String username) {
        String domain = getDomain(username);
        String tenant = getTenant(username);
        return username.substring(domain == null ? 0 : domain.length() + 1,
                username.length() - (tenant == null ? 0 : tenant.length() + 1));
    }
}
