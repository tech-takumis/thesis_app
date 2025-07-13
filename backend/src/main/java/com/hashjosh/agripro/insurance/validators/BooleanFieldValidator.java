package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class BooleanFieldValidator implements  FieldValidatorStrategy{
    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field)throws InvalidApplicationException {
        if(!"true".equalsIgnoreCase(fieldValue) && !"false".equalsIgnoreCase(fieldValue)){
            throw new InvalidApplicationException("Field '" + fieldName + "' must be 'true' or 'false'. ");
        }
    }
}
