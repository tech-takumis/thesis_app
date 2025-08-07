package com.hashjosh.agripro.insurance.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashjosh.agripro.insurance.dto.ApplicationRequest;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.enums.FieldType;
import com.hashjosh.agripro.insurance.service.InsuranceApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class InsuranceApplicationController {

    private final InsuranceApplicationService applicationService;

    public InsuranceApplicationController(InsuranceApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/insurances/{id}/application:submit")
    public ResponseEntity<?> submit(
            @RequestPart("fieldValues") String fieldValuesJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @PathVariable Long id
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON fieldValues into a Map<String, JsonNode>
        Map<String, JsonNode> fieldValues = mapper.readValue(fieldValuesJson, new TypeReference<>() {});
        ApplicationRequest request = new ApplicationRequest(fieldValues, files);

        List<ValidationError> errors = applicationService.submitApplication(request, id);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok("Application submitted successfully");
    }


    @GetMapping("/fields/types")
    public ResponseEntity<?> getFieldTypes(){
        Set<String> fieldTypes = new HashSet<>();
        fieldTypes.add(FieldType.TEXT.name());
        fieldTypes.add(FieldType.NUMBER.name());
        fieldTypes.add(FieldType.DATE.name());
        fieldTypes.add(FieldType.BOOLEAN.name());
        fieldTypes.add(FieldType.LOCATION.name());
        fieldTypes.add(FieldType.MULTI_SELECT.name());
        fieldTypes.add(FieldType.SELECT.name());
        fieldTypes.add(FieldType.SIGNATURE.name());
        fieldTypes.add(FieldType.FILE.name());

        return ResponseEntity.ok(fieldTypes);
    }
}

