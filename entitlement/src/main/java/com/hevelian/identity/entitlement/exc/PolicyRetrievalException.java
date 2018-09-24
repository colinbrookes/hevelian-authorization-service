package com.hevelian.identity.entitlement.exc;

public class PolicyRetrievalException extends Exception {

  public PolicyRetrievalException() {
    super();
  }

  public PolicyRetrievalException(String message) {
    super(message);
  }

  public PolicyRetrievalException(Throwable cause) {
    super(cause);
  }

  public PolicyRetrievalException(String message, Throwable cause) {
    super(message, cause);
  }
}
