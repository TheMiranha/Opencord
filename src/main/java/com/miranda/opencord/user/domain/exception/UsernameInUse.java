package com.miranda.opencord.user.domain.exception;

public class UsernameInUse extends RuntimeException {
    public UsernameInUse(String message) {
        super(message);
    }

    public UsernameInUse() {
        super("Este username já está em uso");
    }
}
