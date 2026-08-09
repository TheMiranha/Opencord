package com.miranda.opencord.friendship.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.domain.ChannelType;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.friendship.application.dto.SendFriendshipRequestCommand;
import com.miranda.opencord.friendship.application.dto.SendFriendshipRequestOutput;
import com.miranda.opencord.friendship.domain.FriendshipEntity;
import com.miranda.opencord.friendship.domain.FriendshipStatus;
import com.miranda.opencord.friendship.domain.exception.FriendshipAlreadyExists;
import com.miranda.opencord.friendship.domain.exception.FriendshipIsPending;
import com.miranda.opencord.friendship.domain.exception.FriendshipToSelf;
import com.miranda.opencord.friendship.infrastructure.service.FriendshipService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SendFriendshipRequestUseCase {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private final ChannelService channelService;

    public SendFriendshipRequestOutput execute(SendFriendshipRequestCommand command) {

        UserEntity requester = userService.findById(command.requester()).orElseThrow(UserNotFound::new);
        UserEntity addressee = userService.findByUsername(command.addresseeUsername()).orElseThrow(UserNotFound::new);

        if (requester.getUsername().equals(addressee.getUsername())) {
            throw new FriendshipToSelf();
        }

        if (friendshipService.checkFriendshipByUsers(requester, addressee).isPresent()) {
            throw new FriendshipAlreadyExists();
        }

        Optional<FriendshipEntity> alreadyPendingByRequester = friendshipService.getPendingFriendRequestByRequesterAndAddreessee(addressee, requester);

        if (alreadyPendingByRequester.isPresent()) {
            FriendshipEntity friendship = alreadyPendingByRequester.get();
            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipService.save(friendship);

            channelService.save(
                    ChannelEntity.builder()
                            .type(ChannelType.DM)
                            .members(
                                    new HashSet<>(Set.of(requester, addressee))
                            )
                            .build()
            );

            return new SendFriendshipRequestOutput(friendship.getId());
        }

        if (!friendshipService.isAbleToSendFriendshipRequest(requester, addressee)) {
            throw new FriendshipIsPending();
        }

        FriendshipEntity friendship = friendshipService.save(
                FriendshipEntity.builder()
                        .requester(requester)
                        .addressee(addressee)
                        .status(FriendshipStatus.PENDING)
                        .build()
        );

        return new SendFriendshipRequestOutput(friendship.getId());
    }
}
