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
  private String property;

  public PageRequestBuilder() {
    this.page = DEFAULT_PAGE;
    this.size = DEFAULT_SIZE;
    this.direction = DEFAULT_DIRECTION;
  }

  /**
   * Set page.
   *
   * @param page Zero-based page index. Default is {@value #DEFAULT_PAGE}.
   * @return   Current builder.
   */
  public PageRequestBuilder page(int page) {
    this.page = page;
    return this;
  }

  /**
   * Set size.
   *
   * @param size The size of the page to be returned. Default is {@value #DEFAULT_SIZE}.
   * @return   Current builder.
   */
  public PageRequestBuilder size(int size) {
    this.size = size;
    return this;
  }

  /**
   * Set direction.
   *
   * @param direction The direction of the sort to be specified. Default is {@link #DEFAULT_DIRECTION}.
   * @return   Current builder.
   */
  public PageRequestBuilder direction(Sort.Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Set property.
   *
   * @param property The property to sort by.
   * @return   Current builder.
   */
  public PageRequestBuilder property(String property) {
    this.property = property;
    return this;
  }

  /**
   * Build {@link PageRequest} object from defined properties.
   *
   * @return New {@link PageRequest} object.
   */
  public PageRequest build() {
    PageRequest pageRequest;
    if (property == null) {
      pageRequest = new PageRequest(page, size);
    } else {
      pageRequest = new PageRequest(page,size,direction,property);
    }
    return pageRequest;
  }
}