package com.hevelian.identity.entitlement.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.hevelian.identity.core.model.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Policy extends AbstractEntity {
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, unique = true)
    private String policyId;
    @Column(nullable = false)
    private PolicyType policyType;
}
