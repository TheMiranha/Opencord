package com.miranda.opencord.channel.infrastructure.service;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
