package com.hevelian.identity.core.exc;

public class CoreRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -3739739988260358396L;

  public CoreRuntimeException() {}

  public CoreRuntimeException(String message) {
    super(message);
  }

  public CoreRuntimeException(Throwable cause) {
    super(cause);
  }

  public CoreRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CoreRuntimeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
