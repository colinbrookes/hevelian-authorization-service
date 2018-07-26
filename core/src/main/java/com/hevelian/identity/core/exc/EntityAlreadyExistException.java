package com.hevelian.identity.core.exc;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityAlreadyExistException extends Exception {

  public EntityAlreadyExistException(String message) {
    super(message);
  }
}
