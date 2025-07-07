package com.hashjosh.agripro.user.dto;

public record LoginResponseDto(
        String jwt,
        String csrfToken
) {
}
