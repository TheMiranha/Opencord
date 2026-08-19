package com.miranda.opencord.friendship.application.dto;

public record FriendRequestAcceptedNotification(
        String type,
        String friendshipId,
        String channelId
) {
    public static FriendRequestAcceptedNotification create(String friendshipId, String channelId) {
        return new FriendRequestAcceptedNotification("FRIEND_REQUEST_ACCEPTED", friendshipId, channelId);
    }
}
