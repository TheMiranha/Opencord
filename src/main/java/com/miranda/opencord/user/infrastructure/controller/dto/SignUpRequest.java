package com.miranda.opencord.user.infrastructure.controller.dto;

import com.miranda.opencord.user.application.dto.SignUpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record SignUpRequest(
        @NotEmpty
        String username,
        @Email
        String email,
        @NotEmpty
        @Length(min = 8, max = 255)
        String password
) {

    public SignUpCommand toCommand() {
        return new SignUpCommand(
                this.username(),
                this.email(),
                this.password()
        );
    }
}
