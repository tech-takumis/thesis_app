package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record PaginatedResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first,
        String next,
        String prev
) {
}
