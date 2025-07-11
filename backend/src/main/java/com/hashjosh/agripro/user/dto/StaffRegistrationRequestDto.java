package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record StaffRegistrationRequestDto(
        @NotNull(message = "Fullname is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String fullname,
        @Email(message = "Email should be a valid!")
        @NotNull(message = "Email is required!")
        String email,

        @NotNull(message = "Gender is required")
        @Size(min = 3, max = 50, message = "Gender must be between 3 and 50 characters")
//        @Pattern(regexp = "^(?i)(MALE|FEMALE)&")
        String gender,
        @NotBlank(message = "Contact number is required")
//        @Pattern(regexp = "^\\+63\\d{2}-\\d{3}-\\d{4}$")
        String contactNumber,
        @NotBlank(message = "Civil Status is required")
        String civilStatus,
        @NotBlank(message = "Address is required!")
        String address,
        @NotBlank(message = "Position is required!")
        String position,
        @NotBlank(message = "Department is required!")
        String department,
        @NotBlank(message = "Location is required!")
        String location,
        Set<RoleDto> roles

) {
}
