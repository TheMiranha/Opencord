package com.miranda.opencord.friendship.application.dto;

public record FriendRequestReceivedNotification(
        String type,
        String friendshipId,
        String senderId,
        String senderUsername
) {
    public static FriendRequestReceivedNotification create(String friendshipId, String senderId, String senderUsername) {
        return new FriendRequestReceivedNotification("FRIEND_REQUEST_RECEIVED", friendshipId, senderId, senderUsername);
    }
}
