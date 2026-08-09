package com.miranda.opencord.friendship.infrastructure.controller;

import com.miranda.opencord.friendship.application.dto.*;
import com.miranda.opencord.friendship.application.usecase.GetFriendshipsUseCase;
import com.miranda.opencord.friendship.application.usecase.SendFriendshipRequestUseCase;
import com.miranda.opencord.friendship.infrastructure.controller.dto.SendFriendshipRequestRequest;
import com.miranda.opencord.user.domain.UserEntity;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final SendFriendshipRequestUseCase sendFriendshipRequestUseCase;
    private final GetFriendshipsUseCase getFriendshipsUseCase;

    @PostMapping("/request")
    public ResponseEntity<SendFriendshipRequestOutput> handleFriendshipRequest(@RequestBody @Valid SendFriendshipRequestRequest body, @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(sendFriendshipRequestUseCase.execute(new SendFriendshipRequestCommand(user.getId(), body.addresseeUsername())));
    }

    @GetMapping()
    public ResponseEntity<List<FriendshipOutput>> handleFriendshipRequest(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(getFriendshipsUseCase.execute(new GetFriendshipsCommand(user.getId())));
    }

}
