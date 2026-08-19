package com.miranda.opencord.user.infrastructure.controller;

import com.miranda.opencord.user.application.dto.UserProfileOutput;
import com.miranda.opencord.user.application.usecase.GetUserProfileUseCase;
import com.miranda.opencord.user.application.usecase.UpdateUserAvatarUseCase;
import com.miranda.opencord.user.application.usecase.UpdateUserProfileUseCase;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.infrastructure.controller.dto.MeResponse;
import com.miranda.opencord.user.infrastructure.controller.dto.UpdateUserProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UpdateUserAvatarUseCase updateUserAvatarUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;

    @GetMapping("/me")
    public ResponseEntity<MeResponse> handleMe(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(new MeResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCustomStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }

    @PutMapping("/me/profile")
    public ResponseEntity<MeResponse> handleUpdateProfile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(updateUserProfileUseCase.execute(user.getId(), request));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileOutput> handleGetUserProfile(
            @PathVariable UUID userId,
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(getUserProfileUseCase.execute(userId, user.getId()));
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeResponse> handleUploadAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(updateUserAvatarUseCase.execute(user.getId(), file));
    }

}
