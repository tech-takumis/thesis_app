package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class IntegerFieldValidator implements  FieldValidatorStrategy{
    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field) throws InvalidApplicationException {
        try{
            Integer.parseInt(fieldValue);
        }catch (NumberFormatException e){
            throw new InvalidApplicationException("Field " + fieldName + " is not an integer");
        }
    }
}
