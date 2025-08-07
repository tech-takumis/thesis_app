package com.hashjosh.agripro.insurance.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.enums.StatusType;
import com.hashjosh.agripro.insurance.model.Insurance;
import com.hashjosh.agripro.insurance.model.InsuranceApplication;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Service;

@Service
public class InsuranceApplicationMapper {

    public InsuranceApplication toInsuranceApplication(JsonNode values,
                                                       Insurance type, User user) {
        return InsuranceApplication.builder()
                .user(user)
                .insurance(type)
                .status(StatusType.PENDING.name())
                .dynamicFields(values)
                .build();
    }
}
