package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.user.application.dto.SignUpCommand;
import com.miranda.opencord.user.application.dto.SignUpOutput;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.EmailInUse;
import com.miranda.opencord.user.domain.exception.UsernameInUse;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SignUpOutput execute(SignUpCommand command) {

        if (this.userService.findByEmailIgnoreCase(command.email()).isPresent()) {
            throw new EmailInUse();
        }

        if (this.userService.findByUsername(command.username()).isPresent()) {
            throw new UsernameInUse();
        }

        UserEntity user = UserEntity.builder()
                .username(command.username())
                .email(command.email())
                .hashedPassword(passwordEncoder.encode(command.password()))
                .build();

        UserEntity savedUser = userService.create(user);

        return new SignUpOutput(savedUser.getId());
    }

}
