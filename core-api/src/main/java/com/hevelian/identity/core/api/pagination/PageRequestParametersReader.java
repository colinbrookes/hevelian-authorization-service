package com.hevelian.identity.core.api.pagination;

import com.hevelian.identity.core.pagination.PageRequestBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read API parameters from request, parse them and initialize a valid {@link PageRequestBuilder} object.
 *
 * @author yshymkiv
 */
public class PageRequestParametersReader {

  private static final Pattern PATTERN = Pattern.compile("^ *(\\w+) +(\\w+) *$");

  /**
   * This method creates a {@link PageRequestBuilder} object from request parameters.
   *
   * @param page page number.
   * @param size page size.
   * @param sort property name with direction of sorting.All spaces are trimmed.
   *             Samples: <ul><li>name ASC</li><li>name DESC</li><li>name</li></ul>.
   * @return {@link PageRequestBuilder} object.
   */
  public PageRequestBuilder readParameters(Integer page, Integer size, String sort) {
    PageRequestBuilder builder = createBuilder();

    if (page != null) {
      builder.page(page);
    }
    if (size != null) {
      builder.size(size);
    }

    SortInfo sortInfo = parseSortExpression(sort);
    if (sortInfo.property != null) {
      builder.property(sortInfo.property);
    }
    if (sortInfo.direction != null) {
      builder.direction(sortInfo.direction);
    }

    return builder;
  }

  /**
   * Build {@link SortInfo} object from defined properties.
   *
   * @param sort The property to sort by.
   * return New {@link SortInfo} object.
   */
  protected SortInfo parseSortExpression(String sort) {
    SortInfo sortInfo = new SortInfo();
    if (!StringUtils.isEmpty(sort)) {
      String property = sort;
      Matcher matcher = PATTERN.matcher(sort);
      if (matcher.matches()) {
        property = matcher.group(1);
        sortInfo.setDirection(Sort.Direction.fromString(matcher.group(2)));
      }
      sortInfo.setProperty(property);
    }
    return sortInfo;
  }

  protected PageRequestBuilder createBuilder() {
    return new PageRequestBuilder();
  }

  /**
   *  Represents sort property with direction.
   */
  @Getter
  @Setter
  public static class SortInfo {
    private String property;
    private Sort.Direction direction;
  }
}