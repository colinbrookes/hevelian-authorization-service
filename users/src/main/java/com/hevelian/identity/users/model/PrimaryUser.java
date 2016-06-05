package com.hevelian.identity.users.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.eclipse.persistence.annotations.Multitenant;

import com.hevelian.identity.core.model.UserCredentials;

import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant
@Getter
@Setter
public class PrimaryUser extends UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Boolean enabled;

    // Users are always retrieved together with roles. Also can be handled on
    // Repository layer, but we will never use users alone.
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

}
