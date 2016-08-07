package com.hevelian.identity.core.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

import org.eclipse.persistence.annotations.Index;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "domain", callSuper = false)
public class Tenant extends AbstractEntity {
    @Column(updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime dateCreated;

    @Column(nullable = false, unique = true, updatable = false)
    @Index
    private String domain;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false, updatable = false)
    private String adminName;

    @Column(nullable = false)
    private String contactEmail;

    @Column
    // TODO handle description length properly
    private String description;

    @PrePersist
    void onCreate() {
        setDateCreated(LocalDateTime.now());
    }
}
