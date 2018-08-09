package com.hevelian.identity.core.api.pagination;

/**
 * Container for API pagination and sorting parameter names with their descriptions.
 *
 * @author yshymkiv
 */
public final class PageRequestParameters {

  public static final String PAGE = "page";
  public static final String SIZE = "size";
  public static final String SORT = "sort";
  public static final int PAGE_MIN = 0;
  public static final int SIZE_MIN = 1;

  // These two values are a String representation of PageRequestBuilder.DEFAULT_PAGE and PageRequestBuilder.DEFAULT_SIZE properties.
  // IMPORTANT. Keep them in sync with referenced properties. They should be compile-time constants to be used in annotations like @ApiParam.
  public static final String DEFAULT_PAGE = "0";
  public static final String DEFAULT_SIZE = "20";

  public static final String PAGE_DESCRIPTION = "Zero-based page index";
  public static final String SIZE_DESCRIPTION = "The size of the page to be returned";
  public static final String SORT_DESCRIPTION = "API supports sorting only by one parameter. Format: 'parameter ASC|DESC' or without sort direction. " +
                                                "Example:'name ASC', 'order DESC', 'dateCreated'";

  private PageRequestParameters() {
  }
}
