package com.hashjosh.agripro.authority.exceptions;

import com.hashjosh.agripro.global.dto.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorityExceptionHandler {

    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorityNotFoundException(AuthorityNotFoundException ex) {
        return ResponseEntity.status(ex.getErrorResponseDto().getStatus()).body(ex.getErrorResponseDto());
    }
}
