package com.hevelian.identity.entitlement.model.pap;

import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import com.hevelian.identity.core.model.HistPK;
import com.hevelian.identity.entitlement.model.Policy;
import lombok.Getter;
import lombok.Setter;

@Entity
@Multitenant(MultitenantType.SINGLE_TABLE)
@Getter
@Setter
// --- No History For Now ---
// @Table(uniqueConstraints = {@UniqueConstraint(
// columnNames = {"policyId", "version", TenantDiscriminatorColumnMetadata.NAME_DEFAULT})})
@IdClass(HistPK.class)
@AttributeOverride(name = Policy.FIELD_POLICY_ID, column = @Column(nullable = false))
public class PAPPolicyHist extends Policy implements IPAPPolicy {
  @Column(nullable = false)
  @Id
  private Integer version;
  @Column(nullable = false)
  private OffsetDateTime startDate;
  @Column
  private OffsetDateTime endDate;
}
