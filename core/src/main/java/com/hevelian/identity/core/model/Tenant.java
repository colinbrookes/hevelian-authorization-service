package com.hevelian.identity.core.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.eclipse.persistence.annotations.Index;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "domain", callSuper = false)
public class Tenant extends AbstractEntity {
    @Column(nullable = false, unique = true, updatable = false)
    @Index
    private String domain;

    @Column(nullable = false)
    private Boolean active;

    @Embedded
    private User tenantAdmin;
}
