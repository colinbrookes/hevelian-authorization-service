package com.hevelian.identity.users.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Multitenant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.hevelian.identity.core.model.AbstractEntity;
import com.hevelian.identity.core.userinfo.UserInfo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant
@Getter
@Setter
@EqualsAndHashCode(of = "name", callSuper = false)
public class User extends AbstractEntity implements UserInfo {

    @Column(nullable = false, unique = true)
    @Index
    private String name;

    @Column(nullable = false)
    // Ignore password in case the entity is used as a dto
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany
    // We do not need to display roles when displaying a user
    @JsonProperty(access = Access.WRITE_ONLY)
    private Set<Role> roles;

    @Column(nullable = false, updatable = false)
    //Lombok cannot work with getters for Boolean
    @Getter(AccessLevel.NONE)
    private Boolean deletable;

    public boolean isDeletable() {
        return deletable;
    }
}
