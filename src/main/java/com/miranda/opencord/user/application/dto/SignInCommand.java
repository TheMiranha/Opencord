package com.miranda.opencord.user.application.dto;

public record SignInCommand(
        String username,
        String password
) {
}
