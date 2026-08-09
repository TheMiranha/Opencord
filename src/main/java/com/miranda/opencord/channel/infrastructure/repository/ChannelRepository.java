package com.miranda.opencord.channel.infrastructure.repository;

import com.miranda.opencord.channel.domain.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<ChannelEntity, UUID> {

    boolean existsByIdAndMembers_Id(UUID channelId, UUID userId);
}
