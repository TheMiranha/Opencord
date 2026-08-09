package com.miranda.opencord.friendship.domain.exception;

public class FriendshipAlreadyExists extends RuntimeException {
    public FriendshipAlreadyExists(String message) {
        super(message);
    }
    public FriendshipAlreadyExists() {
        super("Amizade já existente");
    }
}
