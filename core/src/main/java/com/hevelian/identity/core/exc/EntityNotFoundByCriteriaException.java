package com.hevelian.identity.core.exc;

import lombok.Getter;

@Getter
public class EntityNotFoundByCriteriaException extends Exception {
  private static final long serialVersionUID = 2313582704926199258L;
  private final String criteria;
  private final String value;

  public EntityNotFoundByCriteriaException(String criteria, String value) {
    super(String.format("Entity not found by '%s' equals '%s'.", criteria, value));
    this.criteria = criteria;
    this.value = value;
  }

}
