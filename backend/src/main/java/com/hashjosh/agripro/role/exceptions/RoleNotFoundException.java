package com.hashjosh.agripro.role.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleNotFoundException extends RuntimeException{
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final int status;

    public RoleNotFoundException(String error, int status, String message){
        super(message);
        this.error = error;
        this.status = status;
    }
}
