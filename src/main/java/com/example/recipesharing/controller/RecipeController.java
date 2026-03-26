package com.example.recipesharing.controller;

import com.example.recipesharing.model.Comment;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.UserEntity;
import com.example.recipesharing.repository.CommentRepository;
import com.example.recipesharing.repository.RecipeRepository;
import com.example.recipesharing.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Recipe> getAllRecipes(@RequestParam(value = "search", required = false) String q) {
        if (q != null && !q.isBlank()) {
            return recipeRepository.findByTitleContainingIgnoreCase(q);
        }
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return recipeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestParam("authorId") Long authorId, @Valid @RequestBody Recipe recipe) {
        Optional<UserEntity> author = userRepository.findById(authorId);
        if (author.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("author not found");
        }
        recipe.setAuthor(author.get());
        Recipe saved = recipeRepository.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe updated) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.setTitle(updated.getTitle());
            recipe.setDescription(updated.getDescription());
            recipe.setInstructions(updated.getInstructions());
            Recipe saved = recipeRepository.save(recipe);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return recipeRepository.findById(id)
            .map(recipe -> ResponseEntity.ok(commentRepository.findByRecipe(recipe)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestParam("authorId") Long authorId, @RequestBody Comment comment) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);
        Optional<UserEntity> authorOpt = userRepository.findById(authorId);
        if (recipeOpt.isEmpty() || authorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("recipe or author not found");
        }

        comment.setRecipe(recipeOpt.get());
        comment.setAuthor(authorOpt.get());
        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}