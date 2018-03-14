package com.hevelian.identity.core.api.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import com.hevelian.identity.core.api.validation.DomainValidator;

/**
 * The string has to be a well-formed domain name.
 *
 */
@Documented
@Constraint(validatedBy = DomainValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Domain {
  // TODO localize
  String message() default "Not a well-formed  domain";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
