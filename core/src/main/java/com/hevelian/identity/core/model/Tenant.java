package com.hevelian.identity.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Constraint: to make the 'dateActiveChanged' work properly - update the 'active' property only via
 * the entity object.
 *
 * @author yuflyud
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "domain", callSuper = false)
public class Tenant extends AbstractEntity {

  @Column(nullable = false, updatable = false)
  @Setter(AccessLevel.PRIVATE)
  @FieldNameConstants
  private OffsetDateTime dateCreated;

  @Column(nullable = false)
  @Setter(AccessLevel.PRIVATE)
  private OffsetDateTime dateActiveChanged;

  @Column(nullable = false, unique = true, updatable = false)
  @Index
  @FieldNameConstants
  private String domain;

  @Column(nullable = false)
  @FieldNameConstants
  private Boolean active;

  @Column(nullable = false, updatable = false)
  private String adminName;

  @Column(nullable = false)
  private String contactEmail;

  // Not all JDBC drivers support fetch type LAZY.
  @Lob
  // Logo can be max 360kB
  @Column(length = 360000)
  @JsonIgnore
  private byte[] logo;

  @Column
  // Default length (255) should be fine
  private String description;

  // ADDITIONAL JPA SPECIFIC LOGIC TO HANDLE CREATE DATE AND ACTIVE DATE
  // CHANGED PROPERTIES.
  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @JsonIgnore
  private Boolean _initialActive;

  @PostLoad
  public void afterLoad() {
    _initialActive = active;
  }

  @PrePersist
  public void onCreate() {
    OffsetDateTime now = OffsetDateTime.now();
    setDateCreated(now);
    setDateActiveChanged(now);
  }

  @PreUpdate
  public void onUpdate() {
    if (!Objects.equal(active, _initialActive)) {
      setDateActiveChanged(OffsetDateTime.now());
      _initialActive = active;
    }
  }
  @JsonProperty("hasLogo")
  public boolean hasLogo() {
    return logo!=null;
  }

}
