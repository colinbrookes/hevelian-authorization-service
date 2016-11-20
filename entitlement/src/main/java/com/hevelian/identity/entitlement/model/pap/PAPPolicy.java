package com.hevelian.identity.entitlement.model.pap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Customizer;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hevelian.identity.core.model.jpa.HistoryCustomizer;
import com.hevelian.identity.entitlement.model.Policy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
// See comments in User class.
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "policyId",
        TenantDiscriminatorColumnMetadata.NAME_DEFAULT }) })
@Customizer(HistoryCustomizer.class)
// To make the @LastModifiedBy annotation work
@EntityListeners(AuditingEntityListener.class)
public class PAPPolicy extends Policy implements IPAPPolicy {
    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    // customize the value of Spring data annotations see
    // http://www.baeldung.com/database-auditing-jpa
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    @Version
    private Integer version;

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
}
