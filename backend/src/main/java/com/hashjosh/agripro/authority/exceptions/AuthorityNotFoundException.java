package com.hashjosh.agripro.authority.exceptions;

import com.hashjosh.agripro.global.dto.ErrorResponseDto;

import java.time.LocalDateTime;

public class AuthorityNotFoundException extends RuntimeException{
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final int status;

    public AuthorityNotFoundException(String message, String error, int status) {
        super(message);
        this.error = error;
        this.status = status;
    }

    public ErrorResponseDto getErrorResponseDto() {
        return new ErrorResponseDto(timestamp, status, error, getMessage());
    }
}
