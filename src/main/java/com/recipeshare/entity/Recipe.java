package com.recipeshare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ingredients;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String steps;

    private String coverImageUrl;

    @ElementCollection
    @CollectionTable(name = "recipe_images", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeStatus status = RecipeStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Chef author;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    private Instant approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private Chef approvedBy;

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
