package com.miranda.opencord.channel.infrastructure.service;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.domain.ChannelType;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelEntity save(ChannelEntity channel) {
        return channelRepository.save(channel);
    }

    public boolean isUserMemberOf(UUID userId, UUID channelId) {
        return channelRepository.existsByIdAndMembers_Id(channelId, userId);
    }

    public List<ChannelEntity> getDMChannelsByUser(UUID userId) {
        return channelRepository.findAllWithMembersByUserId(userId)
                .stream()
                .filter(channel -> channel.getType() == ChannelType.DM)
                .toList();
    }

    public List<ChannelEntity> findAllByServerId(UUID serverId) {
        return channelRepository.findAllByServerId(serverId);
    }
}
