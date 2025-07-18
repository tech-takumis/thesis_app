package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.enums.Status;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ApplicationMapper {

    public InsuranceApplication toInsuranceApplication(InsuranceApplicationRequestDto dto,
                                                       InsuranceType insuranceType,
                                                       User user) {
        return InsuranceApplication.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(Status.PENDING)
                .user(user)
                .insuranceType(insuranceType)
                .fieldValues(dto.fieldValues())
                .build();
    }

    public ApplicationResponseDto toApplicationResponse(InsuranceApplication application) {
        return new ApplicationResponseDto(
//                toInsuranceTypeResponse(application.getInsuranceType()),
                application.getStatus().name(),
                application.getFieldValues()
        );
    }
}
