package com.hashjosh.agripro.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDTO(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required!")
//        @Pattern(
//                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
//                message = "Password must have at least 8 characters, including uppercase, lowercase, number, and special character"
//        )
        String password,
        Boolean remember
) {
}
