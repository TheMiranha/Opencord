package com.miranda.opencord.server.infrastructure.repository;

import com.miranda.opencord.server.domain.ServerInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerInviteRepository extends JpaRepository<ServerInviteEntity, UUID> {
    Optional<ServerInviteEntity> findByCode(String code);
    List<ServerInviteEntity> findAllByServerIdOrderByCreatedAtDesc(UUID serverId);
    Optional<ServerInviteEntity> findByServerIdAndCode(UUID serverId, String code);
}