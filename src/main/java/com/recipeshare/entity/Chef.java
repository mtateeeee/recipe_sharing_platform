package com.recipeshare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chefs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String displayName;

    @Column(unique = true)
    private String email;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.VIEWER;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Recipe> recipes = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
