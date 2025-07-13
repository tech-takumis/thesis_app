package com.hashjosh.agripro.role.dtos;

import java.util.Set;

public record RoleResponseDto(
        String name,
        Set<String> authorities,
        Set<String> users
) {
}
