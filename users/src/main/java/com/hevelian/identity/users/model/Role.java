package com.hevelian.identity.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.hevelian.identity.core.model.AbstractEntity;

@Entity
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
