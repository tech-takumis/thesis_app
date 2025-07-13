package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.services.ApplicationInsuranceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InsuranceApplicationController {

    private final ApplicationInsuranceService service;

    public InsuranceApplicationController(ApplicationInsuranceService service) {
        this.service = service;
    }

    @PostMapping("/insurance")
    public ResponseEntity<InsuranceApplication> createInsurance(@RequestBody InsuranceApplication application) {
        return new ResponseEntity<>(service.createApplication(application), HttpStatus.CREATED);
    }
}
