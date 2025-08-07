package com.hashjosh.agripro.role.dtos;

import java.util.Set;

public record RoleResponseDto(
        Long id,
        String name,
        Set<String> authorities
) {
}
