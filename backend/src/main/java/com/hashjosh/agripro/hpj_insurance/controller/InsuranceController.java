package com.hashjosh.agripro.hpj_insurance.controller;


import com.hashjosh.agripro.hpj_insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.hpj_insurance.service.InsuranceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PostMapping
    public ResponseEntity<InsuranceResponseDto> save(
            @RequestBody InsuranceRequestDto dto
    ){
        return new ResponseEntity<>(insuranceService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InsuranceResponseDto>> findAll(){
        return new ResponseEntity<>(insuranceService.findAll(),HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(insuranceService.findById(id), HttpStatus.FOUND);
    }
}
