package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.enums.Status;
import com.hashjosh.agripro.insurance.models.Application;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service
public class ApplicationMapper {

    public Application toInsuranceApplication(Map<String,String> fieldValues,
                                              InsuranceType insuranceType,
                                              User user) {
        return Application.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(Status.PENDING)
                .user(user)
                .insuranceType(insuranceType)
                .fieldValues(fieldValues)
                .build();
    }

    public ApplicationResponseDto toApplicationResponse(Application application) {
        return new ApplicationResponseDto(
//                toInsuranceTypeResponse(application.getInsuranceType()),
                application.getStatus().name(),
                application.getFieldValues()
        );
    }
}
