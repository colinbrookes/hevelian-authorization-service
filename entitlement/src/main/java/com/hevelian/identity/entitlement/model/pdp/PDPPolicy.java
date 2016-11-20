package com.hevelian.identity.entitlement.model.pdp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;

import com.hevelian.identity.entitlement.model.Policy;

import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
// See comments in User class.
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "policyId",
        TenantDiscriminatorColumnMetadata.NAME_DEFAULT }) })
public class PDPPolicy extends Policy {
    @Column(nullable = false)
    private Integer policyOrder = 0;
}
