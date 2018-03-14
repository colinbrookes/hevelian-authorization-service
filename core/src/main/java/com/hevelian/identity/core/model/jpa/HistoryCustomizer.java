package com.hevelian.identity.core.model.jpa;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.HistoryPolicy;

public class HistoryCustomizer implements DescriptorCustomizer {
  public static final String HIST_TABLE_SUFFIX = "HIST";
  public static final String STARTDATE_COLUMN = "STARTDATE";
  public static final String ENDDATE_COLUMN = "ENDDATE";

  public void customize(ClassDescriptor descriptor) {
    HistoryPolicy policy = new HistoryPolicy();
    policy.addHistoryTableName(getHistoryTableName(descriptor));
    policy.addStartFieldName(getStartFieldName());
    policy.addEndFieldName(getEndFieldName());
    descriptor.setHistoryPolicy(policy);
  }

  protected String getStartFieldName() {
    return STARTDATE_COLUMN;
  }

  protected String getEndFieldName() {
    return ENDDATE_COLUMN;
  }

  protected String getHistoryTableName(ClassDescriptor descriptor) {
    return descriptor.getTableName() + HIST_TABLE_SUFFIX;
  }

}
