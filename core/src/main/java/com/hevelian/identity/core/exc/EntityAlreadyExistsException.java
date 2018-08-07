package com.hevelian.identity.core.exc;

public class EntityAlreadyExistsException extends Exception {

  public EntityAlreadyExistsException() {
    super();
  }

  public EntityAlreadyExistsException(String message) {
    super(message);
  }

  public EntityAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  public EntityAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
