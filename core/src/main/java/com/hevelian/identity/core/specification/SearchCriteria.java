package com.hevelian.identity.core.specification;

import lombok.Value;

/**
 * Represents the search parameters objects.
 *
 * @author yshymkiv
 */

@Value
public class SearchCriteria {
  private String key;
  private Object value;
}
