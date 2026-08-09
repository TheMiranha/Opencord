package com.miranda.opencord.friendship.application.dto;

import java.util.List;

public record GetFriendshipsOutput(
        List<FriendshipOutput> friendships
) {
}
