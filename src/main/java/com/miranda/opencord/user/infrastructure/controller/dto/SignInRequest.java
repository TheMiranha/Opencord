package com.miranda.opencord.user.infrastructure.controller.dto;

import com.miranda.opencord.user.application.dto.SignInCommand;

public record SignInRequest(
        String username,
        String password
) {

    public SignInCommand toCommand() {
        return new SignInCommand(
                this.username(),
                this.password()
        );
    }
}
