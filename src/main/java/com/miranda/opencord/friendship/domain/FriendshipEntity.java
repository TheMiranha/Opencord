package com.miranda.opencord.friendship.domain;

import com.miranda.opencord.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "friendships")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
    private UserEntity requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
    private UserEntity addressee;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;


    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.updatedAt = now;
        this.createdAt = now;
    }
}
