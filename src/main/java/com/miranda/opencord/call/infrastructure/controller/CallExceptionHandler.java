package com.miranda.opencord.call.infrastructure.controller;

import com.miranda.opencord.call.domain.exception.IsNotAMember;
import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CallExceptionHandler {

    @ExceptionHandler(IsNotAMember.class)
    public ResponseEntity<ErrorResponse> handlIsNotAMemberException(
            IsNotAMember exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }

}
