package com.hevelian.identity.entitlement.logging;

import org.apache.logging.log4j.Logger;

public class EntitlementLogger {

  private final Logger delegate;

  public EntitlementLogger(Logger delegate) {
    this.delegate = delegate;
  }

  public void logAttributeRequest(String subject, String resource, String action,
                                  String environment) {
    if (delegate.isDebugEnabled()) {
      StringBuilder sb = new StringBuilder("XACML Request Attributes:\n").append("Subject: ")
          .append(subject).append("\nResource: ").append(resource).append("\nAction: ")
          .append(action).append("\nEnvironment: ").append(environment);
      delegate.debug(sb.toString());
    }
  }

  public void logRequest(String request) {
    if (delegate.isDebugEnabled()) {
      delegate.debug("XACML Request:\n" + request);
    }
  }

  public void logResponse(String response) {
    if (delegate.isDebugEnabled()) {
      delegate.debug("XACML Response:\n" + response);
    }
  }
}
