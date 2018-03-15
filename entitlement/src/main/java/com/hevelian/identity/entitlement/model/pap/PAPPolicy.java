package com.hevelian.identity.entitlement.model.pap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import com.hevelian.identity.entitlement.model.Policy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
// See comments in User class.
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"policyId", TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
// --- No History For Now ---
// @Customizer(HistoryCustomizer.class)
public class PAPPolicy extends Policy implements IPAPPolicy {
  @Column(nullable = false)
  @Setter(AccessLevel.PROTECTED)
  @Version
  private Integer version;

}
