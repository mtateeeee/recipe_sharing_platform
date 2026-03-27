package com.recipeshare.service;

import com.recipeshare.dto.RecipeDto;
import com.recipeshare.entity.Chef;
import com.recipeshare.entity.Recipe;
import com.recipeshare.entity.RecipeStatus;
import com.recipeshare.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ImageProcessingService imageProcessingService;

    @Transactional(readOnly = true)
    public Page<Recipe> findApproved(Pageable pageable) {
        return recipeRepository.findByStatus(RecipeStatus.APPROVED, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RecipeDto> findApprovedDtos(Pageable pageable) {
        Page<Recipe> page = recipeRepository.findByStatus(RecipeStatus.APPROVED, pageable);
        return new PageImpl<>(toDtoList(page.getContent()), pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<Recipe> findApprovedAll() {
        return recipeRepository.findByStatus(RecipeStatus.APPROVED);
    }

    @Transactional(readOnly = true)
    public List<Recipe> findPending() {
        return recipeRepository.findByStatus(RecipeStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> findPendingDtos() {
        return toDtoList(recipeRepository.findByStatus(RecipeStatus.PENDING));
    }

    @Transactional(readOnly = true)
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public RecipeDto findDtoById(Long id) {
        return toDto(findById(id));
    }

    @Transactional(readOnly = true)
    public Recipe findByIdAndApproved(Long id) {
        Recipe r = recipeRepository.findById(id).orElse(null);
        return (r != null && r.getStatus() == RecipeStatus.APPROVED) ? r : null;
    }

    @Transactional(readOnly = true)
    public List<Recipe> findByAuthor(Long authorId) {
        return recipeRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> findByAuthorDtos(Long authorId) {
        return toDtoList(recipeRepository.findByAuthorId(authorId));
    }

    @Transactional
    public Recipe create(Chef author, String title, String description, String ingredients, String steps,
                        MultipartFile coverImage, List<MultipartFile> images) throws IOException {
        Recipe recipe = Recipe.builder()
                .title(title)
                .description(description)
                .ingredients(ingredients)
                .steps(steps)
                .author(author)
                .status(RecipeStatus.PENDING)
                .build();
        if (coverImage != null && !coverImage.isEmpty()) {
            recipe.setCoverImageUrl(imageProcessingService.saveAndProcess(coverImage));
        }
        if (images != null && !images.isEmpty()) {
            recipe.setImageUrls(imageProcessingService.saveMultiple(images));
            if (recipe.getCoverImageUrl() == null && !recipe.getImageUrls().isEmpty()) {
                recipe.setCoverImageUrl(recipe.getImageUrls().get(0));
            }
        }
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void approve(Long recipeId, Chef admin) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công thức"));
        recipe.setStatus(RecipeStatus.APPROVED);
        recipe.setApprovedAt(Instant.now());
        recipe.setApprovedBy(admin);
        recipeRepository.save(recipe);
    }

    @Transactional
    public void reject(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công thức"));
        recipe.setStatus(RecipeStatus.REJECTED);
        recipeRepository.save(recipe);
    }

    public RecipeDto toDto(Recipe r) {
        if (r == null) return null;
        return RecipeDto.builder()
                .id(r.getId())
                .title(r.getTitle())
                .description(r.getDescription())
                .ingredients(r.getIngredients())
                .steps(r.getSteps())
                .coverImageUrl(r.getCoverImageUrl())
                .imageUrls(r.getImageUrls() != null ? new ArrayList<>(r.getImageUrls()) : null)
                .status(r.getStatus())
                .authorName(r.getAuthor() != null ? r.getAuthor().getDisplayName() : null)
                .authorId(r.getAuthor() != null ? r.getAuthor().getId() : null)
                .createdAt(r.getCreatedAt())
                .approvedAt(r.getApprovedAt())
                .build();
    }

    public List<RecipeDto> toDtoList(List<Recipe> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}
