package com.miranda.opencord.channel.infrastructure.service;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.domain.ChannelType;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerMemberRepository serverMemberRepository;

    public ChannelEntity save(ChannelEntity channel) {
        return channelRepository.save(channel);
    }

    public boolean isUserMemberOf(UUID userId, UUID channelId) {
        ChannelEntity channel = channelRepository.findById(channelId).orElse(null);
        if (channel == null) return false;

        // Se for canal pertencente a um servidor (SERVER_TEXT / SERVER_VOICE)
        if (channel.getServer() != null) {
            if (channel.getServer().getOwner() != null && channel.getServer().getOwner().getId().equals(userId)) {
                return true;
            }
            return serverMemberRepository.findByServerIdAndUserId(channel.getServer().getId(), userId).isPresent();
        }

        // Se for canal de DM
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
