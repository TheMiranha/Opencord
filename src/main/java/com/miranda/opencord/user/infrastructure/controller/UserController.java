package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.user.application.usecase.UpdateUserAvatarUseCase;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.controller.dto.MeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UpdateUserAvatarUseCase updateUserAvatarUseCase;

    @GetMapping("/me")
    public ResponseEntity<MeResponse> handleMe(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(new MeResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeResponse> handleUploadAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(updateUserAvatarUseCase.execute(user.getId(), file));
    }

}
