package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import com.miranda.opencord.user.domain.exception.EmailInUse;
import com.miranda.opencord.user.domain.exception.UsernameInUse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UsernameInUse.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(
            UsernameInUse exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage(), Instant.now())
        );
    }


    @ExceptionHandler(EmailInUse.class)
    public ResponseEntity<ErrorResponse> handleEmailInUseException(
            EmailInUse exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage(), Instant.now())
        );
    }
}
