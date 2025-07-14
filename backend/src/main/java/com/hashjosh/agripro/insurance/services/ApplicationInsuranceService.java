package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.mappers.ApplicationInsuranceMapper;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceRepository;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceTypeRepository;
import com.hashjosh.agripro.insurance.validators.FieldValidatorStrategy;
import com.hashjosh.agripro.insurance.validators.ValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApplicationInsuranceService {

    private final ApplicationInsuranceRepository repository;
    private final ApplicationInsuranceTypeRepository applicationInsuranceTypeRepository;
    private final ApplicationInsuranceMapper mapper;

    public ApplicationInsuranceService(ApplicationInsuranceRepository repository, ApplicationInsuranceTypeRepository applicationInsuranceTypeRepository, ApplicationInsuranceMapper mapper) {
        this.repository = repository;
        this.applicationInsuranceTypeRepository = applicationInsuranceTypeRepository;
        this.mapper = mapper;
    }


    public InsuranceApplication createApplication(InsuranceApplication application) {

        validateApplication(application);
        return repository.save(application);
    }

    private  void  validateApplication(InsuranceApplication application) throws InvalidApplicationException {
        InsuranceType type = applicationInsuranceTypeRepository.findById(application.getInsuranceType().getId())
                .orElseThrow(() -> new InvalidApplicationException("Application Type Not Found"));

        Map<String,String> submittedValues = application.getFieldValues();

        for(InsuranceField field: type.getFields()){
            String fieldName = field.getKeyName();
            String fieldValue = submittedValues.get(fieldName);

            if (field.is_required() && (fieldValue == null || fieldName.isBlank())) {
                throw new InvalidApplicationException("Missing required field: " + fieldName);
            }

            if(fieldValue != null && !fieldValue.isBlank()){
                FieldValidatorStrategy validator = ValidatorFactory.getValidator(field.getFieldType());

                validator.validate(fieldName, fieldValue,field);
            }
        }

    }
}
