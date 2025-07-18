package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.dto.PaginatedResponseDto;
import com.hashjosh.agripro.insurance.services.InsuranceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }


    @PostMapping("/insurances")
    public ResponseEntity<String> createInsurance(
            @RequestBody InsuranceRequestDto dto
    ){
        return new ResponseEntity<>(service.createInsurance(dto), HttpStatus.CREATED);
    }


    // This  get mapping return all insurances, we're going to  apply pagination here.
    @GetMapping("/insurances/{id}")
    public ResponseEntity<InsuranceResponseDto> findInsurance(
            @PathVariable(required = true) Long id
    ){
        return new ResponseEntity<>(service.findInsurance(id), HttpStatus.FOUND);
    }

    @GetMapping("/insurances")
    public ResponseEntity<PaginatedResponseDto<InsuranceResponseDto>> findAllInsurances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            HttpServletRequest request) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        String baseUrl = request.getRequestURL().toString(); // e.g http://localhost:8000/insurances
        return new ResponseEntity<>(service.findAllInsuranceType(pageable, baseUrl), HttpStatus.FOUND);
    }


    @DeleteMapping("/insurances/{id}")
    public ResponseEntity<String> deleteInsurance(
            @PathVariable(required = true) Long id
    ){
        service.deleteInsurance(id);
        return ResponseEntity.status(HttpStatus.GONE).body("Insurance deleted");
    }
}
