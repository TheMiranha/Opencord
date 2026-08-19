package com.miranda.opencord.channel.application.usecase;

import com.miranda.opencord.channel.application.dto.ServerChannelOutput;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.server.domain.exception.IsNotAServerMember;
import com.miranda.opencord.server.infrastructure.service.ServerMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetServerChannelsUseCase {

    private final ChannelService channelService;
    private final ServerMemberService serverMemberService;

    @Transactional(readOnly = true)
    public List<ServerChannelOutput> execute(UUID serverId, UUID userId) {
        boolean isMember = serverMemberService.findByServerIdAndUserId(serverId, userId).isPresent();
        if (!isMember) {
            throw new IsNotAServerMember();
        }

        return channelService.findAllByServerId(serverId).stream()
                .map(channel -> new ServerChannelOutput(
                        channel.getId(),
                        channel.getName(),
                        channel.getType(),
                        channel.getServer().getId()
                ))
                .collect(Collectors.toList());
    }
}