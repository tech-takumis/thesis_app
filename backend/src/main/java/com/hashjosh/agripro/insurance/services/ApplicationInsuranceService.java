package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.mappers.ApplicationInsuranceMapper;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import com.hashjosh.agripro.user.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationInsuranceService {

    private final InsuranceValidator validator;
    private final InternalInsuranceService internalInsuranceService;
    private final ApplicationInsuranceMapper mapper;


    public ApplicationInsuranceService(InsuranceValidator validator,
                                       InternalInsuranceService internalInsuranceService,
                                       ApplicationInsuranceMapper mapper) {
        this.validator = validator;
        this.internalInsuranceService = internalInsuranceService;
        this.mapper = mapper;
    }


    public String submitApplication(InsuranceApplicationRequestDto dto, Long insuranceTypeId) {
        try{
            InsuranceType insuranceType = internalInsuranceService.findInsuranceTypeById(insuranceTypeId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getPrincipal() == null){
                throw new InvalidApplicationException("Invalid user session");
            }

            User  user = internalInsuranceService.getAuthenticatedUser(authentication.getName());


            System.out.println("User logged in: " + user);
            validator.validateApplication(dto.fieldValues(), insuranceType);
            InsuranceApplication application = mapper.toInsuranceApplication(dto, insuranceType, user);

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
        InsuranceType insurance = internalInsuranceService.findInsuranceTypeById(id);
        return mapper.toInsuranceTypeResponse(insurance);
    }

    public void deleteInsurance(Long id) {
        internalInsuranceService.deleteInsurance(id);
    }

    public List<ApplicationResponseDto> findApplication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User  user = internalInsuranceService.getAuthenticatedUser(authentication.getName());

        List<InsuranceApplication> applications = internalInsuranceService.findAllApplicationByUserId(user.getId());

        return applications.stream().map(mapper::toApplicationResponse).collect(Collectors.toList());
    }
}
