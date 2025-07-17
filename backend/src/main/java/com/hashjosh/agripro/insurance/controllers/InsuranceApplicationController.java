package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.services.ApplicationInsuranceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InsuranceApplicationController {

    private final ApplicationInsuranceService service;

    public InsuranceApplicationController(ApplicationInsuranceService service) {
        this.service = service;
    }

    @PostMapping("/insurances/{id}/application:submit")
    public ResponseEntity<String> submitUserInsurance(
            @PathVariable(required = true) Long id,
            @RequestBody InsuranceApplicationRequestDto application) {
        return new ResponseEntity<>(service.submitApplication(application, id), HttpStatus.CREATED);
    }

    @PostMapping("/insurances")
    public ResponseEntity<String> createInsurance(
            @RequestBody InsuranceRequestDto dto
    ){
        return new ResponseEntity<>(service.createInsurance(dto), HttpStatus.CREATED);
    }
    
    @GetMapping("/insurances/{id}")
    public ResponseEntity<InsuranceResponseDto> findInsurance(
            @PathVariable(required = true) Long id
    ){
        return new ResponseEntity<>(service.findInsurance(id), HttpStatus.FOUND);
    }

    @DeleteMapping("/insurances/{id}")
    public ResponseEntity<String> deleteInsurance(
            @PathVariable(required = true) Long id
    ){
        service.deleteInsurance(id);
        return ResponseEntity.status(HttpStatus.GONE).body("Insurance deleted");
    }
}
