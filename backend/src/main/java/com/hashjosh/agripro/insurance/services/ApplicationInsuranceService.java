package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.mappers.ApplicationInsuranceMapper;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceRepository;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceTypeRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.user.services.InternalUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationInsuranceService {

    private final ApplicationInsuranceTypeRepository applicationInsuranceTypeRepository;
    private final InsuranceValidator validator;
    private final ApplicationInsuranceRepository applicationInsuranceRepository;
    private final InternalInsuranceService internalInsuranceService;
    private final ApplicationInsuranceMapper mapper;
    private final InsuranceTypeRepository insuranceTypeRepository;


    public ApplicationInsuranceService(ApplicationInsuranceTypeRepository applicationInsuranceTypeRepository,
                                       InsuranceValidator validator, ApplicationInsuranceRepository applicationInsuranceRepository,
                                       InternalInsuranceService internalInsuranceService,
                                       ApplicationInsuranceMapper mapper, InsuranceTypeRepository insuranceTypeRepository) {
        this.applicationInsuranceTypeRepository = applicationInsuranceTypeRepository;
        this.validator = validator;
        this.applicationInsuranceRepository = applicationInsuranceRepository;
        this.internalInsuranceService = internalInsuranceService;
        this.mapper = mapper;
        this.insuranceTypeRepository = insuranceTypeRepository;
    }


    public String submitApplication(InsuranceApplicationRequestDto dto, Long insuranceTypeId) {
        try{
            InsuranceType insuranceType = applicationInsuranceTypeRepository.findById(insuranceTypeId)
                    .orElseThrow(() -> new  InvalidApplicationException("Application Type not found"));

            validator.validateApplication(dto.fieldValues(), insuranceType);
            InsuranceApplication application = mapper.toInsuranceApplication(dto, insuranceType);

            // Save the insurance application
            internalInsuranceService.saveInsuranceApplication(application);

            return "Insurance application submitted successfully";
        } catch (Exception e) {
            System.out.printf("Insurance application submitted failed: %s%n", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String createInsurance(InsuranceRequestDto dto) {
        try{

            InsuranceType type = mapper.toInsuranceType(dto);
            InsuranceType savedType = internalInsuranceService.saveInsuranceType(type);

            List<InsuranceField> fields = mapper.requestToInsuranceFields(dto, savedType);
            internalInsuranceService.saveInsuranceFields(fields);

            type.setFields(fields);

            return "Insurance created successfully";
        }catch (Exception e) {
            System.out.println("Insurance creation failed: " + e.getMessage());
            throw new InvalidApplicationException("Insurance creation failed");
        }
    }

    public InsuranceResponseDto findInsurance(Long id) {
        InsuranceType insurance = internalInsuranceService.findInsurance(id);
        return mapper.toInsuranceResponse(insurance);
    }

    public void deleteInsurance(Long id) {
        insuranceTypeRepository.deleteById(id);
    }
}
