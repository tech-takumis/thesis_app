package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.validators.FieldValidatorStrategy;
import com.hashjosh.agripro.insurance.validators.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApplicationValidator {

    public void  validateApplication(Map<String, String> fieldValues, InsuranceType insuranceType) throws InvalidApplicationException {

        for(InsuranceField field: insuranceType.getFields()) {
            String fieldName = field.getKey();
            String fieldValue = fieldValues.get(fieldName);
            String coordinate = field.isHasCoordinate() ? fieldValues.get("coordinate") : null;


            if(field.isHasCoordinate() && coordinate == null) {
                throw  new InvalidApplicationException("Coordinate cannot be null in field " + fieldName);
            }


            if (field.is_required() && (fieldValue == null || fieldName.isBlank())) {
                throw new InvalidApplicationException("Missing required field: " + fieldName);
            }

            if(fieldValue != null && !fieldValue.isBlank()){
                FieldValidatorStrategy validator = ValidatorFactory.getValidator(field.getFieldType());
                System.out.println("field name: " + fieldName + " | type from DB: " + field.getFieldType());
                validator.validate(fieldName, fieldValue,field);
            }
        }

    }
}
