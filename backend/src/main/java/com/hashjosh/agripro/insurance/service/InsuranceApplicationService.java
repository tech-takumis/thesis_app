package com.hashjosh.agripro.insurance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hashjosh.agripro.insurance.dto.ApplicationRequest;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.insurance.mapper.InsuranceApplicationMapper;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import com.hashjosh.agripro.insurance.model.Insurance;
import com.hashjosh.agripro.insurance.model.InsuranceSection;
import com.hashjosh.agripro.insurance.repository.InsuranceApplicationRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.insurance.validation.FieldValidatorFactory;
import com.hashjosh.agripro.insurance.validation.FieldValidatorStrategy;
import com.hashjosh.agripro.insurance.validation.FileValidator;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsuranceApplicationService {
    private final InsuranceApplicationRepository applicationRepo;
    private final InsuranceTypeRepository insuranceTypeRepository;
    private final FieldValidatorFactory fieldValidatorFactory;
    private final ObjectMapper objectMapper;
    private final InsuranceApplicationMapper applicationMapper;
    private final UserRepository userRepository;


    public InsuranceApplicationService(InsuranceApplicationRepository applicationRepo,
                                       InsuranceTypeRepository insuranceTypeRepository,
                                       FieldValidatorFactory fieldValidatorFactory,
                                       ObjectMapper objectMapper,
                                       InsuranceApplicationMapper applicationMapper,
                                       UserRepository userRepository) {
        this.applicationRepo = applicationRepo;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.fieldValidatorFactory = fieldValidatorFactory;
        this.objectMapper = objectMapper;
        this.applicationMapper = applicationMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<ValidationError> submitApplication(ApplicationRequest request, Long applicationId) {
        long start = System.currentTimeMillis();

        ObjectNode mutableFields = objectMapper.convertValue(request.fieldValues(), ObjectNode.class);

        Insurance type = insuranceTypeRepository.findById(applicationId)
                .orElseThrow(() -> new InsuranceApplicationException(
                        "Insurance id " + applicationId + " does not exist",
                        "Insurance does not exist",
                        HttpStatus.NOT_FOUND.value()
                ));

        List<ValidationError> allErrors = new ArrayList<>();

        for(InsuranceSection section: type.getSections()){
            for (InsuranceField field : section.getInsuranceField()) {
                String key = field.getKey();
                JsonNode submittedValue = mutableFields.get(key);

                // Required field validation
                if (field.isRequired() && (submittedValue == null || submittedValue.isNull())) {
                    allErrors.add(new ValidationError(field.getFieldName(), "Field '" + field.getFieldName() + "' is required."));
                    continue;
                }

                if (submittedValue != null && !submittedValue.isNull()) {
                    FieldValidatorStrategy validator = fieldValidatorFactory.getStrategy(field.getFieldType());

                    List<ValidationError> fieldErrors = validator.validate(field, submittedValue);
                    allErrors.addAll(fieldErrors);

                    // Replace with saved file path if file is valid
                    if (fieldErrors.isEmpty() && validator instanceof FileValidator) {
                        String savedPath = FileValidator.saveFile(submittedValue, request.files());
                        mutableFields.put(key, savedPath);  // Replace raw filename with saved path
                    }
                }
            }

            if (!allErrors.isEmpty()) {
                return allErrors;
            }

            // Authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                throw new InsuranceApplicationException("User is not authenticated", "Unauthenticated", HttpStatus.UNAUTHORIZED.value());
            }

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            // Save application with processed values
            JsonNode values = objectMapper.convertValue(mutableFields, JsonNode.class);
            applicationRepo.save(applicationMapper.toInsuranceApplication(values, type, user));

        }
        System.out.println("Time taken to submit application: " + (System.currentTimeMillis() - start) + " ms");

        return List.of();
         // No errors
    }

}
