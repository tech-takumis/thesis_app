package com.hashjosh.agripro.authority.dto;

import java.util.Set;

public record AuthorityResponseDto(
        String name,
        Set<String> roles
) {
}
