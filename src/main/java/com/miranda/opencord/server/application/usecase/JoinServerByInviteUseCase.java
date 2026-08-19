package com.miranda.opencord.server.application.usecase;

import com.miranda.opencord.channel.domain.ChannelEntity;
import com.miranda.opencord.channel.infrastructure.repository.ChannelRepository;
import com.miranda.opencord.server.domain.ServerEntity;
import com.miranda.opencord.server.domain.ServerInviteEntity;
import com.miranda.opencord.server.domain.ServerMemberEntity;
import com.miranda.opencord.server.infrastructure.repository.ServerBanRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerInviteRepository;
import com.miranda.opencord.server.infrastructure.repository.ServerMemberRepository;
import com.miranda.opencord.server.infrastructure.service.ServerInviteService;
import com.miranda.opencord.server.infrastructure.service.ServerMemberService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JoinServerByInviteUseCase {

    private final ServerInviteService serverInviteService;
    private final ServerMemberService serverMemberService;
    private final ServerBanRepository serverBanRepository;
    private final ChannelRepository channelRepository;
    private final UserService userService;

    @Transactional
    public void execute(String code, UUID userId) {
        ServerInviteEntity invite = serverInviteService.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Convite inválido ou expirado"));

        UserEntity user = userService.findById(userId).orElseThrow(UserNotFound::new);
        ServerEntity server = invite.getServer();

        if (serverBanRepository.existsByServerIdAndUserId(server.getId(), userId)) {
            throw new IllegalArgumentException("Você foi banido deste servidor e não pode entrar.");
        }

        boolean isAlreadyMember = serverMemberService.findByServerIdAndUserId(server.getId(), userId).isPresent();
        if (isAlreadyMember) {
            throw new RuntimeException("Você já é membro deste servidor");
        }

        ServerMemberEntity member = ServerMemberEntity.builder()
                .server(server)
                .user(user)
                .role("MEMBER")
                .build();

        serverMemberService.save(member);

        List<ChannelEntity> serverChannels = channelRepository.findAllByServerId(server.getId());
        for (ChannelEntity channel : serverChannels) {
            channel.getMembers().add(user);
            channelRepository.save(channel);
        }
    }
}