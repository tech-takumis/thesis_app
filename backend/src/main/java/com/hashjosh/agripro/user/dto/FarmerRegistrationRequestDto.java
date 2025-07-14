package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FarmerRegistrationRequestDto(
        @NotBlank(message = "RSBSA ID is required")
        String referenceNumber // We gonna create their account using their rsbsa in rsbsa database
) {

}
