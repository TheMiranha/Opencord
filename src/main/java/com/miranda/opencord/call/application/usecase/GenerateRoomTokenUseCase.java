package com.miranda.opencord.call.application.usecase;

import com.miranda.opencord.call.application.dto.GenerateRoomTokenCommand;
import com.miranda.opencord.call.application.dto.GenerateRoomTokenOutput;
import com.miranda.opencord.call.domain.exception.IsNotAMember;
import com.miranda.opencord.call.infrastructure.service.LiveKitTokenService;
import com.miranda.opencord.channel.infrastructure.service.ChannelService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GenerateRoomTokenUseCase {

    private final ChannelService channelService;
    private final LiveKitTokenService mediaTokenService;
    private final UserService userService;

    public GenerateRoomTokenOutput execute(GenerateRoomTokenCommand command) {
        boolean isMember = channelService.isUserMemberOf(command.userId(), command.channelId());

        if (!isMember) {
            throw new IsNotAMember();
        }

        UserEntity user = userService.findById(command.userId()).orElseThrow(UserNotFound::new);

        return new GenerateRoomTokenOutput(
                mediaTokenService.generateJoinToken(command.channelId(), user.getId(), user.getUsername())
        );
    }
}
