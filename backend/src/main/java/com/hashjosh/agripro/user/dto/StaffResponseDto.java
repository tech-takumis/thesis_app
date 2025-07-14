package com.hashjosh.agripro.user.dto;

import java.util.Set;

public record StaffResponseDto(
        String fullname,
        String email,
        Set<String> roles,
        Set<String> permissions,
        String gender,
        String contactNumber,
        String civilStatus,
        String address,
        String position,
        String department,
        String location
) {
}
