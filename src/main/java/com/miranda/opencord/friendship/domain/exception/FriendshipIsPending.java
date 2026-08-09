package com.miranda.opencord.friendship.domain.exception;

public class FriendshipIsPending extends RuntimeException {
    public FriendshipIsPending(String message) {
        super(message);
    }
    public FriendshipIsPending() {
        super("Solicitação de amizade pendente");
    }
}
