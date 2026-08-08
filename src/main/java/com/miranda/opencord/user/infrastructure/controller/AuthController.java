package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.user.application.dto.SignInOutput;
import com.miranda.opencord.user.application.dto.SignUpOutput;
import com.miranda.opencord.user.application.usecase.SignInUseCase;
import com.miranda.opencord.user.application.usecase.SignUpUseCase;
import com.miranda.opencord.user.infrastructure.controller.dto.SignInRequest;
import com.miranda.opencord.user.infrastructure.controller.dto.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpUseCase signUp;
    private final SignInUseCase signIn;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpOutput> handleSignUp(@RequestBody @Valid SignUpRequest payload) {
        return ResponseEntity.ok(signUp.execute(payload.toCommand()));
    }


    @PostMapping("/sign-in")
    public ResponseEntity<SignInOutput> handleSignIn(@RequestBody @Valid SignInRequest payload) {
        return ResponseEntity.ok(signIn.execute(payload.toCommand()));
    }

}
