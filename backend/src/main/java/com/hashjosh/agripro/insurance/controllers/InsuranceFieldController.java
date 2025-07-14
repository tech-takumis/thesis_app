package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.InsuranceFieldRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldResponseDto;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.services.InsuranceFieldService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/insurance/fields")
public class InsuranceFieldController {

    private final InsuranceFieldService service;

    public InsuranceFieldController(InsuranceFieldService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InsuranceFieldResponseDto> createInsuranceField(
            @RequestBody InsuranceFieldRequestDto field) {
        return new ResponseEntity<>(service.saveInsuranceField(field), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllInsuranceFields(
            @RequestParam(required = false)
            @Size(min = 1, max = 5) Long id) {

        System.out.println("ID::: "+id);
        if(id != null) {
            Optional<InsuranceFieldResponseDto> result =   service.findById(id);

            return result.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        List<InsuranceFieldResponseDto> result = service.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInsuranceField(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(service.delete(id), HttpStatus.GONE);
    }

}
