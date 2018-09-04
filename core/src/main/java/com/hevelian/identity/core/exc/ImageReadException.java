package com.hevelian.identity.core.exc;

public class ImageReadException extends RuntimeException {

  public ImageReadException() {
  }

  public ImageReadException(String message) {
    super(message);
  }

  public ImageReadException(Throwable cause) {
    super(cause);
  }

  public ImageReadException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
