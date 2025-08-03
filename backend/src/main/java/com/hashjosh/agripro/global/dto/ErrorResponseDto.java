package com.hashjosh.agripro.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto{
        LocalDateTime timestamp;
        int status;
        String error;
        String message;
}
