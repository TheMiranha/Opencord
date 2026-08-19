package com.miranda.opencord.user.domain;

import com.miranda.opencord.channel.domain.ChannelEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 255)
    String username;

    @Column(nullable = false, length = 255)
    String email;

    @Column(nullable = false, length = 255)
    String hashedPassword;

    @Column(name = "avatar_url", length = 512)
    String avatarUrl;

    @Column(name = "bio", length = 1000)
    String bio;

    @Column(name = "custom_status", length = 255)
    String customStatus;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "channel_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<ChannelEntity> channels = new HashSet<>();

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
