package com.miranda.opencord.server.infrastructure.repository;

import com.miranda.opencord.server.domain.ServerRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerRoleRepository extends JpaRepository<ServerRoleEntity, UUID> {

    List<ServerRoleEntity> findAllByServerIdOrderByPositionAsc(UUID serverId);

    Optional<ServerRoleEntity> findByIdAndServerId(UUID id, UUID serverId);

    @Query("SELECT COALESCE(MAX(r.position), 0) FROM ServerRoleEntity r WHERE r.server.id = :serverId")
    Integer findMaxPositionByServerId(@Param("serverId") UUID serverId);
}
