package com.recipeshare.dto;

import com.recipeshare.entity.RecipeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class RecipeDto {
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String steps;
    private String coverImageUrl;
    private List<String> imageUrls;
    private RecipeStatus status;
    private String authorName;
    private Long authorId;
    private Instant createdAt;
    private Instant approvedAt;
}
