package com.hashjosh.agripro.global.dto;

import java.util.Set;

public record AuthenticatedUserDto(
        String username,
        String fullname,
        String email,
        Set<String> roles
) {
}
