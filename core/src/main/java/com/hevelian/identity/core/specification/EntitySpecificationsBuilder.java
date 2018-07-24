package com.hevelian.identity.core.specification;

import com.hevelian.identity.core.model.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Builder of {@link Specification} objects and creates dynamic query.
 *
 * @author yshymkiv
 */
public class EntitySpecificationsBuilder<T extends AbstractEntity> {

  private final List<SearchCriteria> params;

  public EntitySpecificationsBuilder() {
    params = new ArrayList<>();
  }

  /**
   * Creates {@link SearchCriteria} object and adds him to List parameters.
   *
   * @param key   the name of search parameters
   * @param value search parameters value
   * @return {@link EntitySpecificationsBuilder} object with search parameters list.
   */
  public EntitySpecificationsBuilder with(String key, Object value) {
    params.add(new SearchCriteria(key, value));
    return this;
  }

  /**
   * This method builds dynamic query from {@link SearchCriteria} objects.
   *
   * @return {@link Specification} object .
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
