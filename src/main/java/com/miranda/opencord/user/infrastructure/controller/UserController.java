package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.controller.dto.MeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<MeResponse> handleMe(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(new MeResponse(user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()));
    }

}
