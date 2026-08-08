package com.miranda.opencord.user.domain.exception;

public class EmailInUse extends RuntimeException {
    public EmailInUse(String message) {
        super(message);
    }

    public EmailInUse() {
        super("Este email já está em uso");
    }
}
