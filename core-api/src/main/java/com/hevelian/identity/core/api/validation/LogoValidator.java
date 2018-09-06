package com.hevelian.identity.core.api.validation;

import com.hevelian.identity.core.api.validation.constraints.Logo;
import com.hevelian.identity.core.exc.ImageRetrievalException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Logo image should have the 'image/png' or 'image/jpeg' media types and parameters (width and height) must be less than or equal to 300 pixels.
 */
public class LogoValidator implements ConstraintValidator<Logo, MultipartFile> {

  private static final int MAX_IMAGE_WIDTH = 300;
  private static final int MAX_IMAGE_HEIGHT = 300;

  @Override
  public void initialize(Logo annotation) {
  }

  @Override
  public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    String contentType = multipartFile.getContentType();
    boolean isValid = false;
    if (MediaType.IMAGE_PNG_VALUE.equals(contentType) || MediaType.IMAGE_JPEG_VALUE.equals(contentType)) {
      BufferedImage bufferedImage;
      try {
        bufferedImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
        isValid = (bufferedImage.getHeight() <= MAX_IMAGE_WIDTH && bufferedImage.getWidth() <= MAX_IMAGE_HEIGHT);
      } catch (IOException e) {
        throw new ImageRetrievalException("Error reading image from byte array input stream.", e);
      }
    }
    return isValid;
  }
}
