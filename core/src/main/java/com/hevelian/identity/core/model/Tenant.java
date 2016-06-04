package com.hevelian.identity.core.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tenant extends AbstractEntity {
    @Column(nullable = false, unique = true, updatable = false)
    private String domain;

    @Column(nullable = false)
    private Boolean active;

    @Embedded
    private User tenantAdmin;
}
