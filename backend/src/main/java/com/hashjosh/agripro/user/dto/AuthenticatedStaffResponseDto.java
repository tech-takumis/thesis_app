package com.hashjosh.agripro.user.dto;

import com.hashjosh.agripro.user.models.Role;
import jakarta.validation.constraints.*;

import java.util.Set;

public record AuthenticatedStaffResponseDto(
        String fullname,
        String email,
        Set<String> roles,
        String gender,
        String contactNumber,
        String civilStatus,
        String address,
        String position,
        String department,
        String location

) {
}
