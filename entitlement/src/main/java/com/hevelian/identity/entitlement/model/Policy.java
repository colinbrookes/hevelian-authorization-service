package com.hevelian.identity.entitlement.model;

import com.hevelian.identity.core.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "policyId", callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {Policy.FIELD_POLICY_ID, TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
public abstract class Policy extends AbstractEntity {

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  @FieldNameConstants
  private String policyId;

  @Column(nullable = false)
  @FieldNameConstants
  private PolicyType policyType;

  @Column(nullable = false)
  @Setter(AccessLevel.PROTECTED)
  @LastModifiedDate
  private String lastModifiedDate;

  @Column(nullable = false)
  @Setter(AccessLevel.PROTECTED)
  @LastModifiedBy
  private String lastModifiedBy;
}
