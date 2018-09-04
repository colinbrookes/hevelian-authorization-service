package com.hevelian.identity.entitlement.pdp;

import org.wso2.balana.ParsingException;

public class PolicyParsingException extends ParsingException {
  private static final long serialVersionUID = 6558825735950797015L;

  public PolicyParsingException() {
    super();
  }

  public PolicyParsingException(String message, Throwable cause) {
    super(message, cause);
  }

  public PolicyParsingException(String message) {
    super(message);
  }

  public PolicyParsingException(Throwable cause) {
    super(cause);
  }

}
