package com.hevelian.identity.entitlement.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.hevelian.identity.core.model.AbstractEntity;

@Entity
public class Policy extends AbstractEntity {
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, unique = true)
    private String policyId;
    @Column(nullable = false)
    private PolicyType policyType;

    public Policy() {
        super();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }
}
