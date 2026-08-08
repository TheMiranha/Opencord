package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import com.miranda.opencord.user.domain.exception.EmailInUse;
import com.miranda.opencord.user.domain.exception.InvalidCredentials;
import com.miranda.opencord.user.domain.exception.UsernameInUse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UsernameInUse.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(
            UsernameInUse exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(EmailInUse.class)
    public ResponseEntity<ErrorResponse> handleEmailInUseException(
            EmailInUse exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<ErrorResponse> handleEmailInUseException(
            InvalidCredentials exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    private String getRequestId(HttpServletRequest request) {
        Object requestIdObj = request.getAttribute("requestId");
        return requestIdObj != null ? requestIdObj.toString() : "N/A";
    }
}
