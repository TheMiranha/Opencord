package com.miranda.opencord.server.infrastructure.repository;

import com.miranda.opencord.server.domain.ServerBanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerBanRepository extends JpaRepository<ServerBanEntity, UUID> {

    @Query("""
        SELECT b FROM ServerBanEntity b
        JOIN FETCH b.user
        JOIN FETCH b.bannedBy
        WHERE b.server.id = :serverId
        ORDER BY b.createdAt DESC
    """)
    List<ServerBanEntity> findAllByServerIdWithUsers(@Param("serverId") UUID serverId);

    Optional<ServerBanEntity> findByServerIdAndUserId(UUID serverId, UUID userId);

    boolean existsByServerIdAndUserId(UUID serverId, UUID userId);

    void deleteByServerIdAndUserId(UUID serverId, UUID userId);
}
