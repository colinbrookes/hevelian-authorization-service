package com.hevelian.identity.core.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class User {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    // Ignore password in case the entity is used as a dto
    @Getter(onMethod = @__(@JsonIgnore) )
    private String password;
}
