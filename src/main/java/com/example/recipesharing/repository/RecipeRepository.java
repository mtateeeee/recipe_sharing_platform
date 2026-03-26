package com.example.recipesharing.repository;

import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthor(UserEntity author);
    List<Recipe> findByTitleContainingIgnoreCase(String query);
}