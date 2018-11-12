package com.hevelian.identity.users.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import com.hevelian.identity.core.model.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
@EqualsAndHashCode(of = "domain", callSuper = false)
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"domain", TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
public class UserStore extends AbstractEntity {

  // No unique constraint on name because of
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=499504
  @Column(nullable = false)
  @Index
  private String domain;

  @Column(nullable = false)
  private String className;

  @Column(nullable = false)
  private Boolean enabled;

  @Column
  // Default length (255) should be fine
  private String description;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  private Map<String, String> properties = new HashMap<String, String>();
}
