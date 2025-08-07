package com.hashjosh.agripro.role.dtos;

import com.hashjosh.agripro.authority.Authority;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RoleRequestDto(
        @NotBlank(message = "Role name is required!")
        String name,
        Set<Long> permissionIds
 ) {
}
