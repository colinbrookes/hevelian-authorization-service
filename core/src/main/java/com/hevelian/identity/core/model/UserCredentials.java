package com.hevelian.identity.core.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Index;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class UserCredentials {
    @Column(nullable = false)
    @Index
    private String name;

    @Column(nullable = false)
    // Ignore password in case the entity is used as a dto
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
}
