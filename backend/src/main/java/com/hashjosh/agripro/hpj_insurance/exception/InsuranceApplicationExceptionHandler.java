package com.hashjosh.agripro.hpj_insurance.exception;

import com.hashjosh.agripro.global.dto.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InsuranceApplicationExceptionHandler {

    @ExceptionHandler(InsuranceApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handleInsuranceApplicationException(InsuranceApplicationException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponseDto(
                        ex.getTimestamp(),
                        ex.getStatus(),
                        ex.getError(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InsuranceException.class)
    public ResponseEntity<ErrorResponseDto> handleInsuranceException(InsuranceException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponseDto(
                        ex.getTimestamp(),
                        ex.getStatus(),
                        ex.getError(),
                        ex.getMessage()
                )
        );
    }
}
