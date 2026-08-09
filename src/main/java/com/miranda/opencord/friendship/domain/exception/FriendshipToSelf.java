package com.miranda.opencord.friendship.domain.exception;

public class FriendshipToSelf extends RuntimeException {
    public FriendshipToSelf(String message) {
        super(message);
    }
    public FriendshipToSelf() {
        super("Você não pode enviar uma solicitação de amizade para você mesmo");
    }
}
