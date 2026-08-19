package com.miranda.opencord.user.application.usecase;

import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.UserNotFound;
import com.miranda.opencord.user.infrastructure.controller.dto.MeResponse;
import com.miranda.opencord.user.infrastructure.controller.dto.UpdateUserProfileRequest;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public MeResponse execute(UUID userId, UpdateUserProfileRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (request.bio() != null) {
            user.setBio(request.bio().trim());
        }
        if (request.customStatus() != null) {
            user.setCustomStatus(request.customStatus().trim());
        }

        UserEntity savedUser = userRepository.save(user);

        return new MeResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getAvatarUrl(),
                savedUser.getBio(),
                savedUser.getCustomStatus(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}
