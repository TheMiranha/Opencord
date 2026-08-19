package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.storage.infrastructure.service.MinioStorageService;
import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.controller.dto.MeResponse;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateUserAvatarUseCase {

    private final UserRepository userRepository;
    private final MinioStorageService storageService;

    public MeResponse execute(UUID userId, MultipartFile file) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        String avatarUrl = storageService.uploadAvatar(userId, file);
        user.setAvatarUrl(avatarUrl);
        UserEntity savedUser = userRepository.save(user);

        return new MeResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAvatarUrl(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}
