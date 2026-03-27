package com.recipeshare.controller;

import com.recipeshare.dto.RecipeDto;
import com.recipeshare.entity.Chef;
import com.recipeshare.entity.RecipeStatus;
import com.recipeshare.service.ChefService;
import com.recipeshare.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final ChefService chefService;

    @GetMapping("/public")
    public String publicList(Model model) {
        model.addAttribute("recipes", recipeService.findApprovedDtos(PageRequest.of(0, 24)));
        return "recipes/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        RecipeDto recipe = recipeService.findDtoById(id);
        if (recipe == null || (recipe.getStatus() != RecipeStatus.APPROVED)) {
            return "redirect:/";
        }
        model.addAttribute("recipe", recipe);
        return "recipes/view";
    }

    @GetMapping("/my-recipes")
    public String myRecipes(@AuthenticationPrincipal UserDetails user, Model model) {
        Chef chef = chefService.findByUsername(user.getUsername());
        if (chef == null) return "redirect:/login";
        model.addAttribute("recipes", recipeService.findByAuthorDtos(chef.getId()));
        return "recipes/my-recipes";
    }

    @GetMapping("/create")
    public String createPage(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return "redirect:/login";
        return "recipes/create";
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String ingredients,
            @RequestParam String steps,
            @RequestParam(required = false) MultipartFile coverImage,
            @RequestParam(required = false) List<MultipartFile> images,
            RedirectAttributes ra) {
        Chef chef = chefService.findByUsername(user.getUsername());
        if (chef == null) {
            ra.addFlashAttribute("error", "Vui lòng đăng nhập.");
            return "redirect:/login";
        }
        if (images == null) images = new ArrayList<>();
        try {
            recipeService.create(chef, title, description, ingredients, steps, coverImage, images);
            ra.addFlashAttribute("success", "Công thức đã được gửi và đang chờ admin duyệt.");
            return "redirect:/recipes/my-recipes";
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Lỗi xử lý ảnh: " + e.getMessage());
            return "redirect:/recipes/create";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/recipes/create";
        }
    }
}
