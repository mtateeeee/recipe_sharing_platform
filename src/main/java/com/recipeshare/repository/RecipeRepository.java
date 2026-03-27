package com.recipeshare.repository;

import com.recipeshare.entity.Recipe;
import com.recipeshare.entity.RecipeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findByStatus(RecipeStatus status, Pageable pageable);

    List<Recipe> findByStatus(RecipeStatus status);

    List<Recipe> findByAuthorId(Long authorId);

    List<Recipe> findByAuthorIdAndStatus(Long authorId, RecipeStatus status);
}
