package com.hevelian.identity.core.exc;

public class ReadImageException extends RuntimeException {

  public ReadImageException() {
  }

  public ReadImageException(String message) {
    super(message);
  }

  public ReadImageException(Throwable cause) {
    super(cause);
  }

  public ReadImageException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReadImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
