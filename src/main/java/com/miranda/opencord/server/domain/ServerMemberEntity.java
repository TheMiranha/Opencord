package com.miranda.opencord.server.domain;

import com.miranda.opencord.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "server_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    private ServerEntity server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String role; // "ADMIN", "MEMBER"
}