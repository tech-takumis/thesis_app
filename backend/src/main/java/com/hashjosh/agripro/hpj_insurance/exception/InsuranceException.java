package com.hashjosh.agripro.hpj_insurance.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InsuranceException extends RuntimeException {
   private final LocalDateTime timestamp = LocalDateTime.now();
   private final int status;
   private final String error;

    public InsuranceException(String mgs,int status, String error) {
        super(mgs);
        this.status = status;
        this.error = error;
    }
}
