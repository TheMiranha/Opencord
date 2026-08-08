package com.miranda.opencord.user.application.dto;

public record SignUpCommand(
        String username,
        String email,
        String password
) {
}
