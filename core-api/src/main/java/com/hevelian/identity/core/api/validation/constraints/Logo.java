package com.hevelian.identity.core.api.validation.constraints;

import com.hevelian.identity.core.api.validation.LogoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Logo should match the specified requirements.
 */
@Documented
@Constraint(validatedBy = LogoValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Logo {

  String message() default "Invalid image file! Please, check upon the next requirements: file type is 'image/png' or 'image/jpeg'; width and height not exceed 300 pixels.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
