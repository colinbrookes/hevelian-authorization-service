package com.hevelian.identity.entitlement.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.hevelian.identity.core.model.AbstractEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "policyId", callSuper = false)
public abstract class Policy extends AbstractEntity {
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, unique = true)
    private String policyId;
    @Column(nullable = false)
    private PolicyType policyType;
}
