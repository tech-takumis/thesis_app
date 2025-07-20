package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;

public class BooleanFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
            throw new InvalidApplicationException("Value must be 'true' or 'false'");
        }
    }
}
