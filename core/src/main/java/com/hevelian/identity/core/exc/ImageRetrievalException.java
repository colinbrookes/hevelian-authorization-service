package com.hevelian.identity.core.exc;

public class ImageRetrievalException extends RuntimeException {
  private static final long serialVersionUID = -6359682115318478719L;

  public ImageRetrievalException() {
    super();
  }

  public ImageRetrievalException(String message) {
    super(message);
  }

  public ImageRetrievalException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageRetrievalException(Throwable cause) {
    super(cause);
  }

}
