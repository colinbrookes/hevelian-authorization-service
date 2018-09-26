package com.hevelian.identity.entitlement.model.pdp;

import com.hevelian.identity.core.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
public class PDPConfig extends AbstractEntity {

  @Column(nullable = false)
  private String policyCombiningAlgorithm;

}
