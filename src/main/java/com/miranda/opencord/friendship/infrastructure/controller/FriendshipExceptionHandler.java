package com.miranda.opencord.friendship.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import com.miranda.opencord.friendship.domain.exception.FriendshipAlreadyExists;
import com.miranda.opencord.friendship.domain.exception.FriendshipIsPending;
import com.miranda.opencord.friendship.domain.exception.FriendshipToSelf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FriendshipExceptionHandler {

    @ExceptionHandler(FriendshipIsPending.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(
            FriendshipIsPending exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(FriendshipToSelf.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(
            FriendshipToSelf exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(FriendshipAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(
            FriendshipAlreadyExists exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(exception.getMessage())
        );
    }
}
