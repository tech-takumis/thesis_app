package com.hashjosh.agripro.hpj_insurance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hashjosh.agripro.global.dto.AuthenticatedUserDto;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceApplicationRequest;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.hpj_insurance.mapper.InsuranceMapper;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceType;
import com.hashjosh.agripro.hpj_insurance.repository.InsuranceApplicationRepository;
import com.hashjosh.agripro.hpj_insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.hpj_insurance.validation.FieldValidatorFactory;
import com.hashjosh.agripro.hpj_insurance.validation.FieldValidatorStrategy;
import com.hashjosh.agripro.hpj_insurance.validation.FileValidator;
import com.hashjosh.agripro.user.models.User;
import com.hashjosh.agripro.user.repository.UserRepository;
import com.hashjosh.agripro.user.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class InsuranceApplicationService {
    private final InsuranceApplicationRepository applicationRepo;
    private final InsuranceTypeRepository insuranceTypeRepository;
    private final InsuranceMapper insuranceMapper;
    private final FieldValidatorFactory fieldValidatorFactory;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    public InsuranceApplicationService(InsuranceApplicationRepository applicationRepo,
                                       InsuranceTypeRepository insuranceTypeRepository,
                                       InsuranceMapper insuranceMapper,
                                       FieldValidatorFactory fieldValidatorFactory,
                                       ObjectMapper objectMapper, UserRepository userRepository) {
        this.applicationRepo = applicationRepo;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceMapper = insuranceMapper;
        this.fieldValidatorFactory = fieldValidatorFactory;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public void submitApplication(InsuranceApplicationRequest request, Long applicationId) {
        long start = System.currentTimeMillis();
        ObjectNode mutableFields = objectMapper.convertValue(request.fieldValues(), ObjectNode.class);

        InsuranceType type = insuranceTypeRepository.findById(applicationId)
                .orElseThrow(() -> new InsuranceApplicationException(
                        "Insurance id " + applicationId + "does not exist",
                        "Insurance does not exist",
                        HttpStatus.NOT_FOUND.value()
                ));


        for (InsuranceField field : type.getInsuranceField()) {
            JsonNode submittedValue = mutableFields.get(field.getFieldName());


            if(field.isRequired() && (submittedValue == null || submittedValue.isNull())){
                throw new InsuranceApplicationException(
                        "Field '" + field.getFieldName() + "' is required.",
                        "Field is required",
                        HttpStatus.BAD_REQUEST.value()
                );
            }

            if(submittedValue != null && !submittedValue.isNull()){
                FieldValidatorStrategy validator = fieldValidatorFactory.getStrategy(field.getFieldType());
                validator.validate(field,submittedValue);

                if(validator instanceof FileValidator){
                    String savedPath = FileValidator.saveFile(submittedValue,request.files());

                   mutableFields.put(field.getFieldName(),savedPath);
                }
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null){
            throw new InsuranceApplicationException(
                    "User is not authenticated",
                    "User is not authenticated",
                    HttpStatus.UNAUTHORIZED.value()
            );
        }

        String username = authentication.getName();

        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        JsonNode values = objectMapper.convertValue(mutableFields, JsonNode.class);

        applicationRepo.save(insuranceMapper.toInsuranceApplication(values,type,user));

        System.out.println("Time take to submit application :: " + (System.currentTimeMillis() - start) + " ms");
    }
}
