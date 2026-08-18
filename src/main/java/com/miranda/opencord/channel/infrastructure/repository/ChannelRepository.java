package com.miranda.opencord.channel.infrastructure.repository;

import com.miranda.opencord.channel.domain.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<ChannelEntity, UUID> {

    boolean existsByIdAndMembers_Id(UUID channelId, UUID userId);

    @Query("""
        SELECT DISTINCT c FROM ChannelEntity c
        JOIN FETCH c.members
        WHERE c IN (
            SELECT c2 FROM ChannelEntity c2
            JOIN c2.members m
            WHERE m.id = :userId
        )
    """)
    List<ChannelEntity> findAllWithMembersByUserId(@Param("userId") UUID userId);
}
