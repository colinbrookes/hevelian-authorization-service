package com.hevelian.identity.users.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.experimental.FieldNameConstants;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.hevelian.identity.core.model.AbstractEntity;
import com.hevelian.identity.core.model.UserInfo;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
@EqualsAndHashCode(of = "name", callSuper = false)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {User.FIELD_NAME, TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
public class User extends AbstractEntity implements UserInfo {

  // No unique constraint on name because of
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=499504
  @Column(nullable = false)
  @Index
  @FieldNameConstants
  private String name;

  @Column(nullable = false)
  // Ignore password in case the entity is used as a dto
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

  @Column(nullable = false)
  @FieldNameConstants
  private Boolean enabled;

  @ManyToMany
  // We do not need to display roles when displaying a user
  @JsonProperty(access = Access.WRITE_ONLY)
  private Set<Role> roles;

  @Column(nullable = false, updatable = false)
  // Lombok cannot work with getters for Boolean
  @Getter(AccessLevel.NONE)
  private Boolean deletable;

  public boolean isDeletable() {
    return deletable;
  }
}
