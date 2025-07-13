package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class TextFieldValidator implements  FieldValidatorStrategy{
    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field) throws InvalidApplicationException {
        if(fieldValue.trim().isEmpty()){
            throw new InvalidApplicationException("Field '" + fieldName + "' must not be empty");
        }
    }
}
