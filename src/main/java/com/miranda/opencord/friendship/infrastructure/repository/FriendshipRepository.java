package com.miranda.opencord.friendship.infrastructure.repository;

import com.miranda.opencord.friendship.domain.FriendshipEntity;
import com.miranda.opencord.friendship.domain.FriendshipStatus;
import com.miranda.opencord.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, UUID> {
    List<FriendshipEntity> findByRequesterAndAddressee(UserEntity requester, UserEntity addressee);
    List<FriendshipEntity> getAllByRequesterAndAddressee(UserEntity requester, UserEntity addressee);

    @Query("""
        SELECT f FROM FriendshipEntity f 
        JOIN FETCH f.requester 
        JOIN FETCH f.addressee 
        WHERE (f.requester.id = :userId OR f.addressee.id = :userId) 
          AND f.status = :status
    """)
    List<FriendshipEntity> findFriendshipsByUserAndStatus(
            @Param("userId") UUID userId,
            @Param("status") FriendshipStatus status
    );
}
