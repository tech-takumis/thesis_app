package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class IntegerFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidApplicationException("Value must be a valid integer");
        }
    }
}
