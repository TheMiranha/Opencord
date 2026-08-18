package com.miranda.opencord.friendship.application.usecase;

import com.miranda.opencord.friendship.application.dto.FriendshipOutput;
import com.miranda.opencord.friendship.application.dto.GetFriendshipsCommand;
import com.miranda.opencord.friendship.domain.FriendshipStatus;
import com.miranda.opencord.friendship.infrastructure.service.FriendshipService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPendingFriendshipsUseCase {

    private final FriendshipService friendshipService;
    private final UserService userService;

    public List<FriendshipOutput> execute(GetFriendshipsCommand command) {
        UserEntity user = userService.findById(command.userId()).orElseThrow(UserNotFound::new);

        return friendshipService.findFriendshipsByUserAndStatus(user, FriendshipStatus.PENDING)
                .stream()
                .map(friendship -> {
                    UserEntity friend = friendship.getAddressee().getId() != command.userId() ? friendship.getRequester() : friendship.getAddressee();

                    return new FriendshipOutput(
                            friendship.getId(),
                            friend.getUsername(),
                            friend.getId()
                    );
                })
                .filter(friendshipOutput -> !friendshipOutput.userId().equals(user.getId())).toList();
    }
}
