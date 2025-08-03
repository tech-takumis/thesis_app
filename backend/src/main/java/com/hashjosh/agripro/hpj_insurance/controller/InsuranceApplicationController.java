package com.hashjosh.agripro.hpj_insurance.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceApplicationRequest;
import com.hashjosh.agripro.hpj_insurance.enums.FieldType;
import com.hashjosh.agripro.hpj_insurance.service.InsuranceApplicationService;
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
//            @RequestBody InsuranceApplicationRequest request,
            @RequestPart("fieldValues") String fieldValuesJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @PathVariable Long id
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonNode> fieldValues = mapper.readValue(fieldValuesJson, new TypeReference<>() {});

        // Create record manually
        InsuranceApplicationRequest request = new InsuranceApplicationRequest(fieldValues, files);

        applicationService.submitApplication(request,id);
        return ResponseEntity.ok().build();
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

