package com.supermarket.catalog.domain.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    protected User() {
        // for JPA
    }

    public User(UUID id, String username, String password, String email, Instant joinedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.joinedAt = joinedAt;
    }

    // getters only (no setters!)
}