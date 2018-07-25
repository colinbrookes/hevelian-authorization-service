package com.hevelian.identity.core.specification;

import com.hevelian.identity.core.exc.NotImplementedException;
import com.hevelian.identity.core.model.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Creates {@link Predicate} objects for Data Base query.
 *
 * @author yshymkiv
 */
public class EntitySpecification<T extends AbstractEntity> implements Specification<T> {
  private final static String FROM_DATE = "From";
  private final static int LENGTH_STR_FROM = 4;
  private final static String TO_DATE = "To";
  private final static int LENGTH_STR_TO = 2;

  private SearchCriteria criteria;

  public EntitySpecification(SearchCriteria param) {
    this.criteria = param;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    String key = criteria.getKey();
    Object value = criteria.getValue();

    if (value == null) {
      return null;
    }

    Predicate predicate = null;
    if (root.getModel().getAttributes().stream().anyMatch(a -> Objects.equals(a.getName(), key))) {
      Class<?> javaType = root.get(key).getJavaType();
      if (javaType == String.class) {
        predicate = builder.like(builder.lower(root.get(key)),
            "%" + value.toString().toLowerCase() + "%");
      } else if (javaType == Boolean.class) {
        predicate = builder.equal(root.get(key), value);
      } else if (javaType == Integer.class) {
        predicate = builder.equal(root.get(key), value);
      } else if (value.getClass().isEnum()) {
        predicate = builder.equal(root.get(key), value);
      } else {
        throw new NotImplementedException("Filter by type '" + javaType + "' not implemented.");
      }
    } else if (value instanceof LocalDate && root.get(processedDateKey(key))!= null) {
      String dateKey = processedDateKey(key);
      OffsetDateTime o = OffsetDateTime.of((LocalDate) value,LocalTime.NOON, ZoneOffset.UTC);
      if (key.endsWith(FROM_DATE)) {
        predicate = builder.greaterThanOrEqualTo(root.get(dateKey), o);
      } else if (key.endsWith(TO_DATE)) {
        predicate = builder.lessThanOrEqualTo(root.get(dateKey), o);
      } else {
        throw new NotImplementedException("Filter by name '" + key + "' not implemented.");
      }
    }
    return predicate;
  }

  @NotNull
  private String processedDateKey(String key) {
    int keyLength = key.length();
    String modifyKey;
    if (key.endsWith(FROM_DATE)) {
      modifyKey = key.substring(0, keyLength - LENGTH_STR_FROM);
    } else if (key.endsWith(TO_DATE)) {
      modifyKey = key.substring(0, keyLength - LENGTH_STR_TO);
    } else {
      modifyKey = key;
    }
    return modifyKey;
  }
}
