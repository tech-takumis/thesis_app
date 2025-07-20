package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.mappers.ApplicationMapper;
import com.hashjosh.agripro.insurance.models.Application;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final internalApplicationService internalApplicationService;
    private final ApplicationValidator validator;
    private final ApplicationMapper mapper;
    private final FileStorageService fileStorageService;

    public ApplicationService(internalApplicationService internalApplicationService, ApplicationValidator validator, ApplicationMapper mapper, FileStorageService fileStorageService) {
        this.internalApplicationService = internalApplicationService;
        this.validator = validator;
        this.mapper = mapper;
        this.fileStorageService = fileStorageService;
    }

    public String submitApplication(Map<String, String> fieldValues, List<MultipartFile> files, Long insuranceTypeId) {
        try {
            InsuranceType insuranceType = internalApplicationService.findInsuranceTypeById(insuranceTypeId);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || auth.getPrincipal() == null) {
                throw new InvalidApplicationException("Invalid user session");
            }

            User user = internalApplicationService.getAuthenticatedUser(auth.getName());

            // Process files first and map them
            Map<String, String> fileFieldMapping = new HashMap<>();
            if (files != null) {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    String storedPath = fileStorageService.saveFile(file);
                    fileFieldMapping.put(originalName, storedPath); // map filename → storage path
                }
            }

            // Replace string values in fieldValues that refer to files
            for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
                String value = entry.getValue();
                if (fileFieldMapping.containsKey(value)) {
                    entry.setValue(fileFieldMapping.get(value)); // replace with actual path
                }
            }

            System.out.println("Field Values:: "+ fieldValues);

            validator.validateApplication(fieldValues, insuranceType);

            Application application = mapper.toInsuranceApplication(fieldValues, insuranceType, user);
            internalApplicationService.saveInsuranceApplication(application);

            return "Insurance application submitted successfully";
        } catch (Exception e) {
            System.err.printf("Application failed: %s%n", e.getMessage());
            throw new RuntimeException("Application submission failed", e);
        }
    }



    // This find all application of the authenticated use
    public List<ApplicationResponseDto> findApplication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User  user = internalApplicationService.getAuthenticatedUser(authentication.getName());

        List<Application> applications = internalApplicationService.findAllApplicationByUserId(user.getId());

        return applications.stream().map(mapper::toApplicationResponse).collect(Collectors.toList());
    }
}
