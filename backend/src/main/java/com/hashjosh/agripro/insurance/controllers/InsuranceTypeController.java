package com.hashjosh.agripro.insurance.controllers;

import com.hashjosh.agripro.insurance.dto.InsuranceTypeRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceTypeResponseDto;
import com.hashjosh.agripro.insurance.mappers.InsuranceTypeMapper;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.services.InsuranceTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/insurance/types")
public class InsuranceTypeController {

    private final InsuranceTypeService insuranceTypeService;

    public InsuranceTypeController(InsuranceTypeService insuranceTypeService) {
        this.insuranceTypeService = insuranceTypeService;
    }

    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(required = false)
            @Positive(message = "ID must be a positive number")
            Long id
    ){

        if (id != null) {
            Optional<InsuranceTypeResponseDto> result = insuranceTypeService.findById(id);
            return result.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());

        }

        List<InsuranceTypeResponseDto> results =  insuranceTypeService.getAll();

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InsuranceTypeResponseDto> save(@RequestBody @Valid InsuranceTypeRequestDto dto){
        return new ResponseEntity<>(insuranceTypeService.savedInsurance(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid InsuranceTypeRequestDto dto){
        return  insuranceTypeService.update(id,dto);
    }

}
