package com.miranda.opencord.server.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import com.miranda.opencord.server.domain.exception.IsNotAServerMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(IsNotAServerMember.class)
    public ResponseEntity<ErrorResponse> handleIsNotAServerMemberException(
            IsNotAServerMember exception
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(exception.getMessage())
        );
    }
}
