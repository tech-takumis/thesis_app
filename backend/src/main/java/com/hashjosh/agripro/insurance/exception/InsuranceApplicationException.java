package com.hashjosh.agripro.insurance.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InsuranceApplicationException extends RuntimeException {
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final String error;
        private final int status;

    public InsuranceApplicationException(String msg,String error, int status) {
        super(msg);
        this.error = error;
        this.status = status;
    }
}
