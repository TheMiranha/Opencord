package com.miranda.opencord.friendship.application.dto;

import java.util.UUID;

public record SendFriendshipRequestCommand(
  UUID requester,
  String addresseeUsername
) {
}
