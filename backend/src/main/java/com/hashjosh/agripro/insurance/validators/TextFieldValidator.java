package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class TextFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidApplicationException("Text field cannot be blank");
        }
    }
}

