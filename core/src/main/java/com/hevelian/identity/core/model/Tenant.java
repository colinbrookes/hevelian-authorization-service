package com.hevelian.identity.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Tenant extends AbstractEntity {
    @Column(nullable = false, unique = true, updatable = false)
    private String domain;

    @Column(nullable = false)
    private Boolean active;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
