package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.user.application.dto.SignInCommand;
import com.miranda.opencord.user.application.dto.SignInOutput;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.InvalidCredentials;
import com.miranda.opencord.user.infrastructure.service.TokenService;
import com.miranda.opencord.user.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public SignInOutput execute(SignInCommand command) {
        UserEntity user = userService.findByUsername(command.username()).orElseThrow(InvalidCredentials::new);
        if (!passwordEncoder.matches(command.password(), user.getHashedPassword())) {
            throw new InvalidCredentials();
        }

        return new SignInOutput(tokenService.generateToken(user));
    }
}
