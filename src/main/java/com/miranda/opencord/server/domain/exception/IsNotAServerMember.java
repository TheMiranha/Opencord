package com.miranda.opencord.server.domain.exception;

public class IsNotAServerMember extends RuntimeException {
    public IsNotAServerMember(String message) {
        super(message);
    }
    public IsNotAServerMember() {
        super("Você não é membro deste servidor");
    }
}
