package com.hevelian.identity.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.hevelian.identity.core.model.AbstractEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "name", callSuper = false)
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
