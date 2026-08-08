package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.user.application.dto.SignUpCommand;
import com.miranda.opencord.user.application.dto.SignUpOutput;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserService userService;

    public SignUpOutput execute(SignUpCommand command) {
        UserEntity user = UserEntity.builder()
                .username(command.username())
                .email(command.email())
                .hashedPassword(command.password())
                .build();

        UserEntity savedUser = userService.create(user);

        return new SignUpOutput(savedUser.getId());
    }

}
