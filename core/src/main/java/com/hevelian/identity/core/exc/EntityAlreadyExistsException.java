package com.hevelian.identity.core.exc;

public class EntityAlreadyExistsException extends Exception {

  public EntityAlreadyExistsException() {
    super();
  }

  public EntityAlreadyExistsException(String message) {
    super(message);
  }
}
