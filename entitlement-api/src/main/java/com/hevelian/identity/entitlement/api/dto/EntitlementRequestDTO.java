package com.hevelian.identity.entitlement.api.dto;

import javax.validation.constraints.NotNull;

public class EntitlementRequestDTO {
    @NotNull
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

}
