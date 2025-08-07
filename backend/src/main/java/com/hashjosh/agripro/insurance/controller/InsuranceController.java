package com.hashjosh.agripro.insurance.controller;


import com.hashjosh.agripro.insurance.dto.InsuranceFieldDto;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.service.InsuranceService;
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
        return new ResponseEntity<>(insuranceService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(insuranceService.findById(id), HttpStatus.FOUND);
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<InsuranceResponseDto> updateInsuranceType(@PathVariable Long id,
                                                                        @RequestBody InsuranceRequestDto dto){
        return new ResponseEntity<>(insuranceService.updateInsuranceType(id,dto), HttpStatus.FOUND);
    }

    @PutMapping("/fields/{id}")
    public ResponseEntity<InsuranceFieldResponseDto> updateInsuranceField(
            @PathVariable Long id,
            @RequestBody InsuranceFieldDto dto
            ){
        return new ResponseEntity<>(insuranceService.updateInsuranceFields(id,dto),HttpStatus.OK);

    }

}
