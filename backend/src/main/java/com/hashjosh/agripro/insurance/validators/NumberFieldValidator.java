package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class NumberFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new InvalidApplicationException("Value must be a valid number");
        }
    }
}
