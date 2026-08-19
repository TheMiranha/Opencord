package com.miranda.opencord.server.infrastructure.controller;

import com.miranda.opencord.channel.application.dto.ServerChannelOutput;
import com.miranda.opencord.channel.application.usecase.GetServerChannelsUseCase;
import com.miranda.opencord.server.application.dto.*;
import com.miranda.opencord.server.application.usecase.*;
import com.miranda.opencord.server.infrastructure.controller.dto.CreateServerRequest;
import com.miranda.opencord.user.domain.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {

    private final CreateServerUseCase createServerUseCase;
    private final GetMyServersUseCase getMyServersUseCase;
    private final GetServerChannelsUseCase getServerChannelsUseCase;
    private final GenerateServerInviteUseCase generateServerInviteUseCase;
    private final GetInviteDetailsUseCase getInviteDetailsUseCase;
    private final JoinServerByInviteUseCase joinServerByInviteUseCase;
    private final GetServerInvitesUseCase getServerInvitesUseCase;
    private final DeleteServerInviteUseCase deleteServerInviteUseCase;
    private final GetServerMembersUseCase getServerMembersUseCase;

    @PostMapping
    public ResponseEntity<CreateServerOutput> createServer(
            @Valid @RequestBody CreateServerRequest request,
            @AuthenticationPrincipal UserEntity user) {
        CreateServerCommand command = new CreateServerCommand(request.name(), user.getId());
        CreateServerOutput response = createServerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/@me")
    public ResponseEntity<List<ServerOutput>> getMyServers(@AuthenticationPrincipal UserEntity user) {
        List<ServerOutput> servers = getMyServersUseCase.execute(user.getId());
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/{serverId}/members")
    public ResponseEntity<List<ServerMemberOutput>> getServerMembers(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {

        List<ServerMemberOutput> members = getServerMembersUseCase.execute(serverId, user.getId());
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{serverId}/channels")
    public ResponseEntity<List<ServerChannelOutput>> getServerChannels(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {

        List<ServerChannelOutput> channels = getServerChannelsUseCase.execute(serverId, user.getId());
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/{serverId}/invites")
    public ResponseEntity<GenerateInviteOutput> generateInvite(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {

        GenerateInviteOutput response = generateServerInviteUseCase.execute(serverId, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{serverId}/invites")
    public ResponseEntity<List<ServerInviteOutput>> getServerInvites(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {

        List<ServerInviteOutput> response = getServerInvitesUseCase.execute(serverId, user.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{serverId}/invites/{code}")
    public ResponseEntity<Void> deleteServerInvite(
            @PathVariable UUID serverId,
            @PathVariable String code,
            @AuthenticationPrincipal UserEntity user) {

        deleteServerInviteUseCase.execute(serverId, code, user.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invites/{code}")
    public ResponseEntity<InviteDetailsOutput> getInviteDetails(@PathVariable String code) {
        InviteDetailsOutput response = getInviteDetailsUseCase.execute(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invites/{code}/accept")
    public ResponseEntity<Void> acceptInvite(
            @PathVariable String code,
            @AuthenticationPrincipal UserEntity user) {
        joinServerByInviteUseCase.execute(code, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}