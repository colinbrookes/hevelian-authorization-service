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

  String message() default "Image width and height should not exceed 300 pixels";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
