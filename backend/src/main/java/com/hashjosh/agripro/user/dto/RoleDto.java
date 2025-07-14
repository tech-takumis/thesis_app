package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RoleDto(
        @NotBlank(message = "Role name is required!")
        String name,
        Set<AuthorityDto> authorities
 ) {
}
