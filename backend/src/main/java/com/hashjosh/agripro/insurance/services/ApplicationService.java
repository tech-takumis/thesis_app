package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.mappers.ApplicationMapper;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final internalApplicationService internalApplicationService;
    private final ApplicationValidator validator;
    private final ApplicationMapper mapper;

    public ApplicationService(internalApplicationService internalApplicationService, ApplicationValidator validator, ApplicationMapper mapper) {
        this.internalApplicationService = internalApplicationService;
        this.validator = validator;
        this.mapper = mapper;
    }

    public String submitApplication(InsuranceApplicationRequestDto dto, Long insuranceTypeId) {
        try{
            InsuranceType insuranceType = internalApplicationService.findInsuranceTypeById(insuranceTypeId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getPrincipal() == null){
                throw new InvalidApplicationException("Invalid user session");
            }

            User user = internalApplicationService.getAuthenticatedUser(authentication.getName());


            System.out.println("User logged in: " + user);
            validator.validateApplication(dto.fieldValues(), insuranceType);
            InsuranceApplication application = mapper.toInsuranceApplication(dto, insuranceType, user);

            // Save the insurance application
            internalApplicationService.saveInsuranceApplication(application);

            return "Insurance application submitted successfully";
        } catch (Exception e) {
            System.out.printf("Insurance application submitted failed: %s%n", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // This find all application of the authenticated use
    public List<ApplicationResponseDto> findApplication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User  user = internalApplicationService.getAuthenticatedUser(authentication.getName());

        List<InsuranceApplication> applications = internalApplicationService.findAllApplicationByUserId(user.getId());

        return applications.stream().map(mapper::toApplicationResponse).collect(Collectors.toList());
    }
}
