package com.miranda.opencord.friendship.infrastructure.service;

import com.miranda.opencord.friendship.domain.FriendshipEntity;
import com.miranda.opencord.friendship.domain.FriendshipStatus;
import com.miranda.opencord.friendship.infrastructure.repository.FriendshipRepository;
import com.miranda.opencord.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    public Boolean haveFriendshipRequestByStatus(UserEntity requester, UserEntity addressee, FriendshipStatus status) {
        List<FriendshipEntity> friendships = friendshipRepository.findByRequesterAndAddressee(requester, addressee);
        return friendships.stream().anyMatch(friendship -> friendship.getStatus() == status);
    }

    public Boolean haveAcceptedFriendshipRequest(UserEntity requester, UserEntity addressee) {
       return haveFriendshipRequestByStatus(requester, addressee, FriendshipStatus.ACCEPTED);
    }

    public Boolean haveIgnoredFriendshipRequest(UserEntity requester, UserEntity addressee) {
        return haveFriendshipRequestByStatus(requester, addressee, FriendshipStatus.IGNORED);
    }

    public Boolean isAbleToSendFriendshipRequest(UserEntity requester, UserEntity addressee) {
        return friendshipRepository.getAllByRequesterAndAddressee(requester, addressee).isEmpty();
    }

    public FriendshipEntity save(FriendshipEntity friendship) {
        return friendshipRepository.save(friendship);
    }

    public Optional<FriendshipEntity> getPendingFriendRequestByRequesterAndAddreessee(UserEntity requester, UserEntity addressee) {
        return friendshipRepository.getAllByRequesterAndAddressee(requester, addressee).stream().filter(friendship -> friendship.getStatus().equals(FriendshipStatus.IGNORED) || friendship.getStatus().equals((FriendshipStatus.PENDING))).findFirst();
    }

    public Optional<FriendshipEntity> checkFriendshipByUsers(UserEntity userOne, UserEntity userTwo) {
        List<FriendshipEntity> friendships = friendshipRepository.findByRequesterAndAddressee(userOne, userTwo);
        List<FriendshipEntity> friendships2 = friendshipRepository.findByRequesterAndAddressee(userTwo, userOne);
        friendships.addAll(friendships2);

        return friendships.stream().filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED).findFirst();
    }

    public List<FriendshipEntity> findFriendshipsByUserAndStatus(UserEntity user, FriendshipStatus status) {
        return friendshipRepository.findFriendshipsByUserAndStatus(user.getId(), status);
    }

}
