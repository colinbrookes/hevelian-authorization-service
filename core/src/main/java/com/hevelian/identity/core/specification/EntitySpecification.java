package com.hevelian.identity.core.specification;

import com.hevelian.identity.core.exc.IllegalEntityStateException;
import com.hevelian.identity.core.exc.NotImplementedException;
import com.hevelian.identity.core.model.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.Objects;

/**
 * Creates {@link Predicate} objects for query.
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
    String parameter = criteria.getKey();
    Object value = criteria.getValue();

    if (value == null) {
      return null;
    }

    Predicate predicate = null;
    if (root.getModel().getAttributes().stream().anyMatch(a -> Objects.equals(a.getName(), parameter))) {
      Class<?> javaType = root.get(parameter).getJavaType();
      if (javaType == String.class) {
        predicate = builder.like(builder.lower(root.get(parameter)),
            "%" + value.toString().toLowerCase() + "%");
      } else if (javaType == Boolean.class) {
        predicate = builder.equal(root.get(parameter), value);
      } else if (javaType == Integer.class) {
        predicate = builder.equal(root.get(parameter), value);
      } else if (value.getClass().isEnum()) {
        predicate = builder.equal(root.get(parameter), value);
      } else {
        throw new NotImplementedException("Filter by type '" + javaType + "' not implemented.");
      }
    } else {
      try {
        if (value instanceof LocalDateTime && root.get(processingSearchParameter(parameter)) != null) {
          String dateParameter = processingSearchParameter(parameter);

          LocalDateTime localDateTime = (LocalDateTime) value;
          ZonedDateTime zonedDateTime = ZonedDateTime.ofLocal(localDateTime, ZoneId.systemDefault(), ZoneOffset.UTC);
          OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();

          if (parameter.endsWith(FROM_DATE)) {
            predicate = builder.greaterThanOrEqualTo(root.get(dateParameter), offsetDateTime);
          } else if (parameter.endsWith(TO_DATE)) {
            predicate = builder.lessThan(root.get(dateParameter), offsetDateTime);
          } else {
            throw new NotImplementedException("Filter by name '" + parameter + "' not implemented.");
          }
        }
      } catch (IllegalEntityStateException e) {
        e.printStackTrace();
      }
    }
    return predicate;
  }

  @NotNull
  private String processingSearchParameter(String parameter) throws IllegalEntityStateException {
    int parameterLength = parameter.length();
    String modifyParameter;
    if (parameter.endsWith(FROM_DATE)) {
      modifyParameter = parameter.substring(0, parameterLength - LENGTH_STR_FROM);
    } else if (parameter.endsWith(TO_DATE)) {
      modifyParameter = parameter.substring(0, parameterLength - LENGTH_STR_TO);
    } else {
      throw new IllegalEntityStateException("Incorrect date filter parameter");
    }
    return modifyParameter;
  }
}
