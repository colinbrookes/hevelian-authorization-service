package com.hevelian.identity.users.model;

import com.hevelian.identity.core.model.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
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
@EqualsAndHashCode(of = "name", callSuper = false)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {Role.FIELD_NAME, TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
public class Role extends AbstractEntity {

  // No unique constraint on name because of
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=499504
  @Column(nullable = false)
  @FieldNameConstants
  private String name;
}
