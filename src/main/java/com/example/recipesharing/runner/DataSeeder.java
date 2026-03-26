package com.example.recipesharing.runner;

import com.example.recipesharing.model.Comment;
import com.example.recipesharing.model.Recipe;
import com.example.recipesharing.model.UserEntity;
import com.example.recipesharing.repository.CommentRepository;
import com.example.recipesharing.repository.RecipeRepository;
import com.example.recipesharing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;

    public DataSeeder(UserRepository userRepository, RecipeRepository recipeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        UserEntity alice = userRepository.save(new UserEntity("alice", "Alice Chef", "alice@example.com", "password"));
        UserEntity bob = userRepository.save(new UserEntity("bob", "Bob Baker", "bob@example.com", "password"));

        Recipe r1 = recipeRepository.save(new Recipe("Classic Pancakes", "Fluffy, easy pancakes", "1. Mix flour and sugar...\n2. Add milk and eggs...\n3. Cook on griddle.", alice));
        Recipe r2 = recipeRepository.save(new Recipe("Tomato Basil Pasta", "Fresh tomato pasta", "1. Boil pasta...\n2. Simmer tomatoes with garlic...\n3. Toss with basil.", bob));

        commentRepository.save(new Comment("Love this recipe! So easy and tasty.", r1, bob));
        commentRepository.save(new Comment("Perfect for a quick dinner!", r2, alice));
    }
}