package com.miranda.opencord.call.domain.exception;

public class IsNotAMember extends RuntimeException {
    public IsNotAMember(String message) {
        super(message);
    }
    public IsNotAMember() {
        super("Você não é um membro deste canal");
    }
}
