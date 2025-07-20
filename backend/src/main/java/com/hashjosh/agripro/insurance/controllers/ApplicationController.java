package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponseDto>> findApplications(
    ){
        return new ResponseEntity<>(service.findApplication(), HttpStatus.FOUND);
    }

    @PostMapping(value = "/insurances/{id}/application:submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> submitUserInsurance(
            @PathVariable Long id,
            @RequestPart("fieldValues") Map<String, String> fieldValues,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        String response = service.submitApplication(fieldValues, files, id);

        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }


}
