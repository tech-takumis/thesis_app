package com.hashjosh.agripro.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorityDto(
        @NotBlank(message = "Aauthority name is required!")
        String name
) {
}
