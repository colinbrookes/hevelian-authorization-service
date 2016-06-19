package com.hevelian.identity.users.model;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.eclipse.persistence.annotations.Multitenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.hevelian.identity.core.model.UserCredentials;

import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant
@AttributeOverride(column = @Column(nullable = false, unique = true) , name = "name")
@Getter
@Setter
public class User extends UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany
    // We do not need to display roles when displaying a user
    @JsonProperty(access = Access.WRITE_ONLY)
    private Set<Role> roles;

}
