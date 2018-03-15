package com.hevelian.identity.entitlement.model;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.hevelian.identity.core.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "policyId", callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public abstract class Policy extends AbstractEntity {
  @Column(nullable = false)
  private String content;
  @Column(nullable = false, unique = true)
  private String policyId;
  @Column(nullable = false)
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
