package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class NumberFieldValidator implements  FieldValidatorStrategy{

    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field) throws InvalidApplicationException {
        try{
            Double.parseDouble(fieldValue);
        }catch (NumberFormatException ex){
            throw new InvalidApplicationException("Field " + fieldName + " is not a number");
        }
    }

}
