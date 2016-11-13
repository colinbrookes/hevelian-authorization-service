package com.hevelian.identity.core.api.validation;

import java.net.IDN;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.hevelian.identity.core.api.validation.constraints.Domain;

public class DomainValidator implements ConstraintValidator<Domain, CharSequence> {

    @Override
    public void initialize(Domain annotation) {
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return org.apache.commons.validator.routines.DomainValidator.getInstance()
                .isValid(IDN.toASCII(value.toString()));
    }
}
