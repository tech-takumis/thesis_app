package com.hashjosh.agripro.role.dtos;

import java.util.Set;

public record RoleCreationResponseDto(
        String name,
        Set<String> authorities
) {
}
