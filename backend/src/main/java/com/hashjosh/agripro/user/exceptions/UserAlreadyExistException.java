package com.hashjosh.agripro.user.exceptions;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class UserAlreadyExistException extends RuntimeException {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;

    public UserAlreadyExistException(String message, int status, String error){
        super(message);
        this.status = status;
        this.error = error;
    }
}
