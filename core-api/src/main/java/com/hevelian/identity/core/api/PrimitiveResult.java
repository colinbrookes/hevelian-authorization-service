package com.hevelian.identity.core.api;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrimitiveResult<T> implements Serializable {
  private static final long serialVersionUID = 1817325084758139175L;
  @Getter
  private final T result;
}
