package com.miranda.opencord.server.infrastructure.repository;

import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerMemberRepository extends JpaRepository<ServerMemberEntity, UUID> {

    Optional<ServerMemberEntity> findByServerIdAndUserId(UUID serverId, UUID userId);

    @Query("SELECT DISTINCT sm FROM ServerMemberEntity sm JOIN FETCH sm.user LEFT JOIN FETCH sm.roles WHERE sm.server.id = :serverId")
    List<ServerMemberEntity> findAllByServerId(@Param("serverId") UUID serverId);

    @Query("""
        SELECT DISTINCT sm1.server FROM ServerMemberEntity sm1
        WHERE sm1.user.id = :user1Id
        AND sm1.server.id IN (
            SELECT sm2.server.id FROM ServerMemberEntity sm2
            WHERE sm2.user.id = :user2Id
        )
    """)
    List<ServerEntity> findMutualServers(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}
