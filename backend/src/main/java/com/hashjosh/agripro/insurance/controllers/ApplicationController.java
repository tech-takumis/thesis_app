package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/insurances/{id}/application:submit")
    public ResponseEntity<String> submitUserInsurance(
            @PathVariable(required = true) Long id,
            @RequestBody InsuranceApplicationRequestDto application) {
        return new ResponseEntity<>(service.submitApplication(application, id), HttpStatus.CREATED);
    }
}
