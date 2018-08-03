package com.hevelian.identity.core.specification;

import com.hevelian.identity.core.model.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Builds {@link Specification} objects.
 *
 * @author yshymkiv
 */
public class EntitySpecificationsBuilder<T extends AbstractEntity> {

  private final List<SearchCriteria> params;

  public EntitySpecificationsBuilder() {
    params = new ArrayList<>();
  }

  /**
   * Creates {@link SearchCriteria} object and adds it to a List of parameters.
   *
   * @param parameter the name of a search parameter
   * @param value the value of a search parameter
   * @return a list of {@link SearchCriteria} objects.
   */
  public EntitySpecificationsBuilder with(String parameter, Object value) {
    params.add(new SearchCriteria(parameter, value));
    return this;
  }

  /**
   * Builds {@link Specification} object which contains dynamic query.
   *
   * @return {@link Specification} object.
   */
  public Specification<T> build() {

    List<Specification<T>> specs = new ArrayList<>();
    for (SearchCriteria param : params) {
      specs.add(new EntitySpecification<>(param));
    }

    Specification<T> result;
    Iterator<Specification<T>> i = specs.iterator();
    if (!i.hasNext()) {
      result = null;
    } else {
      result = i.next();
      while (i.hasNext()) {
        result = Specifications.where(result).and(i.next());
      }
    }
    return result;
  }
}
