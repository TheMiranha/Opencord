package com.miranda.opencord.user.domain.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
    public UserNotFound() {
        super("Usuário não encontrado");
    }
}
