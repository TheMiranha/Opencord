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

    // Roles & Moderation Use Cases
    private final CreateServerRoleUseCase createServerRoleUseCase;
    private final GetServerRolesUseCase getServerRolesUseCase;
    private final UpdateServerRoleUseCase updateServerRoleUseCase;
    private final DeleteServerRoleUseCase deleteServerRoleUseCase;
    private final ReorderServerRolesUseCase reorderServerRolesUseCase;
    private final AssignMemberRoleUseCase assignMemberRoleUseCase;
    private final RemoveMemberRoleUseCase removeMemberRoleUseCase;
    private final KickServerMemberUseCase kickServerMemberUseCase;
    private final BanServerMemberUseCase banServerMemberUseCase;
    private final GetServerBansUseCase getServerBansUseCase;
    private final UnbanServerMemberUseCase unbanServerMemberUseCase;

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

    // --- CARGOS (ROLES) ---

    @GetMapping("/{serverId}/roles")
    public ResponseEntity<List<ServerRoleOutput>> getServerRoles(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(getServerRolesUseCase.execute(serverId, user.getId()));
    }

    @PostMapping("/{serverId}/roles")
    public ResponseEntity<ServerRoleOutput> createServerRole(
            @PathVariable UUID serverId,
            @Valid @RequestBody com.miranda.opencord.server.infrastructure.controller.dto.CreateServerRoleRequest request,
            @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createServerRoleUseCase.execute(serverId, request, user.getId()));
    }

    @PutMapping("/{serverId}/roles/{roleId}")
    public ResponseEntity<ServerRoleOutput> updateServerRole(
            @PathVariable UUID serverId,
            @PathVariable UUID roleId,
            @Valid @RequestBody com.miranda.opencord.server.infrastructure.controller.dto.UpdateServerRoleRequest request,
            @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(updateServerRoleUseCase.execute(serverId, roleId, request, user.getId()));
    }

    @DeleteMapping("/{serverId}/roles/{roleId}")
    public ResponseEntity<Void> deleteServerRole(
            @PathVariable UUID serverId,
            @PathVariable UUID roleId,
            @AuthenticationPrincipal UserEntity user) {
        deleteServerRoleUseCase.execute(serverId, roleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{serverId}/roles/reorder")
    public ResponseEntity<List<ServerRoleOutput>> reorderServerRoles(
            @PathVariable UUID serverId,
            @RequestBody com.miranda.opencord.server.infrastructure.controller.dto.ReorderRolesRequest request,
            @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(reorderServerRolesUseCase.execute(serverId, request, user.getId()));
    }

    @PostMapping("/{serverId}/members/{memberId}/roles/{roleId}")
    public ResponseEntity<Void> assignMemberRole(
            @PathVariable UUID serverId,
            @PathVariable UUID memberId,
            @PathVariable UUID roleId,
            @AuthenticationPrincipal UserEntity user) {
        assignMemberRoleUseCase.execute(serverId, memberId, roleId, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{serverId}/members/{memberId}/roles/{roleId}")
    public ResponseEntity<Void> removeMemberRole(
            @PathVariable UUID serverId,
            @PathVariable UUID memberId,
            @PathVariable UUID roleId,
            @AuthenticationPrincipal UserEntity user) {
        removeMemberRoleUseCase.execute(serverId, memberId, roleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    // --- MODERAÇÃO (KICK & BAN) ---

    @PostMapping("/{serverId}/members/{memberId}/kick")
    public ResponseEntity<Void> kickMember(
            @PathVariable UUID serverId,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserEntity user) {
        kickServerMemberUseCase.execute(serverId, memberId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{serverId}/members/{memberId}/ban")
    public ResponseEntity<Void> banMember(
            @PathVariable UUID serverId,
            @PathVariable UUID memberId,
            @RequestBody(required = false) com.miranda.opencord.server.infrastructure.controller.dto.BanMemberRequest request,
            @AuthenticationPrincipal UserEntity user) {
        banServerMemberUseCase.execute(serverId, memberId, request, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{serverId}/bans")
    public ResponseEntity<List<ServerBanOutput>> getServerBans(
            @PathVariable UUID serverId,
            @AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(getServerBansUseCase.execute(serverId, user.getId()));
    }

    @DeleteMapping("/{serverId}/bans/{userId}")
    public ResponseEntity<Void> unbanMember(
            @PathVariable UUID serverId,
            @PathVariable UUID userId,
            @AuthenticationPrincipal UserEntity user) {
        unbanServerMemberUseCase.execute(serverId, userId, user.getId());
        return ResponseEntity.noContent().build();
    }
}