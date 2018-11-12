package com.hevelian.identity.users.stores;

public class UserStoreException extends Exception {
  private static final long serialVersionUID = 476281738759867115L;

  public UserStoreException() {}

  public UserStoreException(String message) {
    super(message);
  }

  public UserStoreException(Throwable cause) {
    super(cause);
  }

  public UserStoreException(String message, Throwable cause) {
    super(message, cause);
  }
}
