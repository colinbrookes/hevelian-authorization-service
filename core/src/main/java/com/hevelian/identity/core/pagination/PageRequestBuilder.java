package com.hevelian.identity.core.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Builder of {@link PageRequest} objects. Specifies default values if not defined for pagination and sorting.
 *
 * @author yshymkiv
 */
@Service
public class PageRequestBuilder {

 public static final int DEFAULT_PAGE = 0;
 public static final int DEFAULT_SIZE = 20;
 public static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;

  private int page;
  private int size;
  private Sort.Direction direction;
  private String sortParameter;

  public PageRequestBuilder() {
    this.page = DEFAULT_PAGE;
    this.size = DEFAULT_SIZE;
    this.direction = DEFAULT_DIRECTION;
  }

  /**
   * Set page.
   *
   * @param page zero-based page index. Default is {@value #DEFAULT_PAGE}.
   * @return   Current builder.
   */
  public PageRequestBuilder page(int page) {
    this.page = page;
    return this;
  }

  /**
   * Set size.
   *
   * @param size size of the page to be returned. Default is {@value #DEFAULT_SIZE}.
   * @return   current builder.
   */
  public PageRequestBuilder size(int size) {
    this.size = size;
    return this;
  }

  /**
   * Set direction.
   *
   * @param direction direction of the sort to be specified. Default is {@link #DEFAULT_DIRECTION}.
   * @return   current builder.
   */
  public PageRequestBuilder direction(Sort.Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Set sort parameter.
   *
   * @param parameter parameter to sort by.
   * @return   current builder.
   */
  public PageRequestBuilder parameter(String parameter) {
    this.sortParameter = parameter;
    return this;
  }

  /**
   * Build {@link PageRequest} object from defined properties.
   *
   * @return new {@link PageRequest} object.
   */
  public PageRequest build() {
    PageRequest pageRequest;
    if (sortParameter == null) {
      pageRequest = new PageRequest(page, size);
    } else {
      pageRequest = new PageRequest(page,size,direction, sortParameter);
    }
    return pageRequest;
  }
}
