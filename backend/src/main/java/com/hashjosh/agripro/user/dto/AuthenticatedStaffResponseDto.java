package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.*;

public record AuthenticatedStaffResponseDto(
        String fullname,
        String email,
        String role,
        String gender,
        String contactNumber,
        String civilStatus,
        String address,
        String position,
        String department,
        String location

) {
}
