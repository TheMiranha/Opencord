package com.miranda.opencord.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

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
