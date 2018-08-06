package com.hevelian.identity.core.api.validation;

import com.hevelian.identity.core.api.validation.constraints.Logo;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LogoValidator implements ConstraintValidator<Logo, MultipartFile> {

  private static final int MAX_IMAGE_WIDTH = 300;
  private static final int MAX_IMAGE_HEIGHT = 300;

  @Override
  public void initialize(Logo annotation) {}

  @Override
  public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    BufferedImage bufferedImage;
    boolean isValid = false;
    try {
      bufferedImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
      isValid = (bufferedImage.getHeight() <= MAX_IMAGE_WIDTH && bufferedImage.getWidth() <= MAX_IMAGE_HEIGHT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return isValid;
  }
}
