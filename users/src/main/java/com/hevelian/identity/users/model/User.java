package com.hevelian.identity.users.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class User {
    @Id
    private String name;

    @Column(nullable = false)
    @Setter(onMethod = @__({ @JsonIgnore }) )
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    // Users are always retrieved together with roles. Also can be handled on
    // Repository layer, but we will never use users alone.
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
