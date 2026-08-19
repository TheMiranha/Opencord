package com.miranda.opencord.server.infrastructure.repository;

import com.miranda.opencord.server.domain.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ServerRepository extends JpaRepository<ServerEntity, UUID> {

    @Query("SELECT s FROM ServerEntity s JOIN s.members m WHERE m.user.id = :userId")
    List<ServerEntity> findServersByUserId(@Param("userId") UUID userId);

}
