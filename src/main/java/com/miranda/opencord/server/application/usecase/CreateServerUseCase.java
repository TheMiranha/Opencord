package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.domain.ChannelType;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.server.application.dto.CreateServerCommand;
import com.miranda.opencord.server.application.dto.CreateServerOutput;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.service.ServerMemberService;
import com.miranda.opencord.server.infrastructure.service.ServerService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateServerUseCase {

    private final ServerService serverService;
    private final UserService userService;
    private final ServerMemberService serverMemberService;
    private final ChannelService channelService;

    public CreateServerOutput execute(CreateServerCommand command) {

        UserEntity user = userService.findById(command.ownerId()).orElseThrow(UserNotFound::new);

        ServerEntity server = ServerEntity.builder()
                .name(command.name())
                .owner(user)
                .build();

        server = serverService.save(server);

        ServerMemberEntity ownerMember = ServerMemberEntity.builder()
                .server(server)
                .user(user)
                .role("ADMIN")
                .build();

        serverMemberService.save(ownerMember);

        Set<UserEntity> initialMembers = new HashSet<>();
        initialMembers.add(user);

        ChannelEntity textChannel = ChannelEntity.builder()
                .name("geral")
                .type(ChannelType.SERVER_TEXT)
                .server(server)
                .members(initialMembers)
                .build();

        ChannelEntity voiceChannel = ChannelEntity.builder()
                .name("Voz Geral")
                .type(ChannelType.SERVER_VOICE)
                .server(server)
                .members(initialMembers)
                .build();

        channelService.save(textChannel);
        channelService.save(voiceChannel);

        return new CreateServerOutput(
                server.getId(),
                server.getName(),
                server.getIconUrl(),
                user.getId()
        );
    }
}