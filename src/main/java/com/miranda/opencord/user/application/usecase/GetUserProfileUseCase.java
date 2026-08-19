package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.friendship.domain.FriendshipEntity;
import com.miranda.opencord.friendship.domain.FriendshipStatus;
import com.miranda.opencord.friendship.infrastructure.repository.FriendshipRepository;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.user.application.dto.MutualServerOutput;
import com.miranda.opencord.user.application.dto.UserProfileOutput;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final UserRepository userRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional(readOnly = true)
    public UserProfileOutput execute(UUID targetUserId, UUID requesterId) {
        UserEntity targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFound::new);

        // 1. Servidores em comum
        List<ServerEntity> mutualServerEntities = serverMemberRepository.findMutualServers(requesterId, targetUserId);
        List<MutualServerOutput> mutualServers = mutualServerEntities.stream()
                .map(s -> new MutualServerOutput(s.getId(), s.getName(), s.getIconUrl()))
                .toList();

        // 2. Amigos em comum
        List<FriendshipEntity> requesterFriendships = friendshipRepository.findFriendshipsByUserAndStatus(requesterId, FriendshipStatus.ACCEPTED);
        List<FriendshipEntity> targetFriendships = friendshipRepository.findFriendshipsByUserAndStatus(targetUserId, FriendshipStatus.ACCEPTED);

        Set<UUID> requesterFriendIds = new HashSet<>();
        for (FriendshipEntity f : requesterFriendships) {
            UUID friendId = f.getRequester().getId().equals(requesterId) ? f.getAddressee().getId() : f.getRequester().getId();
            requesterFriendIds.add(friendId);
        }

        Set<UUID> targetFriendIds = new HashSet<>();
        for (FriendshipEntity f : targetFriendships) {
            UUID friendId = f.getRequester().getId().equals(targetUserId) ? f.getAddressee().getId() : f.getRequester().getId();
            targetFriendIds.add(friendId);
        }

        // Interseção dos IDs de amigos
        requesterFriendIds.retainAll(targetFriendIds);
        int mutualFriendsCount = requesterFriendIds.size();

        return new UserProfileOutput(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getEmail(),
                targetUser.getAvatarUrl(),
                targetUser.getBio(),
                targetUser.getCustomStatus(),
                targetUser.getCreatedAt(),
                targetUser.getUpdatedAt(),
                mutualFriendsCount,
                mutualServers,
                mutualServers.size()
        );
    }
}
