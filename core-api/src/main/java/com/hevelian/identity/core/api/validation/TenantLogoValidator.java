package com.hevelian.identity.core.api.validation;

import lombok.Getter;
import java.awt.image.BufferedImage;

/**
 * Validates logo. Throws {@link IllegalLogoException} when incorrect image parameters is present.
 *
 * @author yshymkiv
 */
public class TenantLogoValidator {

  private static final int MAX_IMAGE_WIDTH = 300;
  private static final int MAX_IMAGE_HEIGHT = 300;
  private static final int MAX_IMAGE_SIZE = 360000;

  /**
   * Checks image parameters(width, height, size). This parameters must meet
   * the following criteria: width and height less than 300 pixels, size - less 360 kB.
   * When one of logo parameters does not meet the criteria throws {@link IllegalLogoException}.
   *
   * @param  bufferedImage tenant logo.
   * @param  size logo size
   * @return      {@link IllegalLogoException}
   */
  public static void imageValidator(BufferedImage bufferedImage, long size) throws IllegalLogoException {
    int height = bufferedImage.getHeight();
    int width = bufferedImage.getWidth();
    if (height > MAX_IMAGE_WIDTH || width > MAX_IMAGE_HEIGHT || size > MAX_IMAGE_SIZE) {
      throw new IllegalLogoException(width, height,size);
    }
  }

  @Getter
  public static class IllegalLogoException extends com.hevelian.identity.core.exc.IllegalLogoException {
    private final int width;
    private final int height;
    private final long size;

    public IllegalLogoException(int width, int height, long size) {
      super();
      this.width = width;
      this.height = height;
      this.size = size;
    }
  }
}
