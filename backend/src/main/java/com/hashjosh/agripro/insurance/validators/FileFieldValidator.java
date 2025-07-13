package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class FileFieldValidator implements  FieldValidatorStrategy{

    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field) throws InvalidApplicationException {
       if (fieldValue == null || fieldValue.isBlank()){
           throw new InvalidApplicationException("Field'" + fieldName +"' requires  a file upload");
       }
    }
}
