package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FarmerRegistrationRequestDto(
        @NotBlank(message = "RSBSA ID is required")
        @NotNull
        String rsbsaId // We gonna create their account using their rsbsa in rsbsa database
) {

}
