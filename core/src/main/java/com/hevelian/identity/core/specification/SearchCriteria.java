package com.hevelian.identity.core.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the search parameters objects.
 *
 * @author yshymkiv
 */
@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
  private String key;
  private Object value;
}
