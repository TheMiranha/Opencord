package com.miranda.opencord.user.domain.exception;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String message) {
        super(message);
    }

    public InvalidCredentials() {
        super("Invalid credentials");
    }
}
