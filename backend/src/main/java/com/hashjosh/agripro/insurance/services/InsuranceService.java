package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.dto.PaginatedResponseDto;
import com.hashjosh.agripro.insurance.mappers.InsuranceMapper;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService {

    private final InternalInsuranceService internalInsuranceService;
    private final InsuranceMapper mapper;
    private final InsuranceTypeRepository insuranceTypeRepository;


    public InsuranceService(InternalInsuranceService internalInsuranceService,
                            InsuranceMapper mapper, InsuranceTypeRepository insuranceTypeRepository) {
        this.internalInsuranceService = internalInsuranceService;
        this.mapper = mapper;
        this.insuranceTypeRepository = insuranceTypeRepository;
    }

    public String createInsurance(InsuranceRequestDto dto) {
        try{

            InsuranceType type = mapper.toInsuranceType(dto);
            InsuranceType savedType = internalInsuranceService.saveInsuranceType(type);

            List<InsuranceField> fields = mapper.requestToInsuranceFields(dto, savedType);
            internalInsuranceService.saveInsuranceFields(fields);

            type.setFields(fields);

            return "Insurance created successfully";
        }catch (Exception e) {
            System.out.println("Insurance creation failed: " + e.getMessage());
            throw new InvalidApplicationException("Insurance creation failed");
        }
    }

    public InsuranceResponseDto findInsurance(Long id) {
        InsuranceType insurance = internalInsuranceService.findInsuranceTypeById(id);
        return mapper.toInsuranceTypeResponse(insurance);
    }

    public void deleteInsurance(Long id) {
        internalInsuranceService.deleteInsurance(id);
    }


    public PaginatedResponseDto<InsuranceResponseDto> findAllInsuranceType(Pageable pageable, String baseUrl) {
        Page<InsuranceType> page = insuranceTypeRepository.findAll(pageable);
        Page<InsuranceResponseDto> dtoPage = page.map(mapper::toInsuranceTypeResponse);
        return mapper.wrapPage(dtoPage,baseUrl);
    }
}
