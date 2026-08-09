package com.miranda.opencord.channel.application.usecase;

import com.miranda.opencord.channel.application.dto.ChannelMemberOutput;
import com.miranda.opencord.channel.application.dto.ChannelOutput;
import com.miranda.opencord.channel.application.dto.GetDMChannelsCommand;
import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetDMChannelsUseCase {

    private final ChannelService channelService;

    public List<ChannelOutput> execute(GetDMChannelsCommand command) {
        List<ChannelEntity> channels = channelService.getDMChannelsByUser(command.userId());

        return channels.stream()
                .map(channel -> {
                    List<ChannelMemberOutput> members = channel.getMembers()
                            .stream()
                            .map(member -> new ChannelMemberOutput(
                                member.getId(), member.getUsername()
                            ))
                            .toList();

                    return new ChannelOutput(
                            channel.getId(),
                            members,
                            Instant.now()
                    );
                })
                .toList();

    }

}
