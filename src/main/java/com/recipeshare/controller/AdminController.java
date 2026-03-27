package com.recipeshare.controller;

import com.recipeshare.entity.Chef;
import com.recipeshare.service.ChefService;
import com.recipeshare.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RecipeService recipeService;
    private final ChefService chefService;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails user, Model model) {
        Chef admin = chefService.findByUsername(user.getUsername());
        if (admin == null) return "redirect:/login";
        model.addAttribute("pendingRecipes", recipeService.findPendingDtos());
        return "admin/dashboard";
    }

    @PostMapping("/recipes/{id}/approve")
    public String approve(@PathVariable Long id, @AuthenticationPrincipal UserDetails user, RedirectAttributes ra) {
        Chef admin = chefService.findByUsername(user.getUsername());
        if (admin == null) return "redirect:/login";
        try {
            recipeService.approve(id, admin);
            ra.addFlashAttribute("success", "Đã duyệt công thức.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/recipes/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes ra) {
        try {
            recipeService.reject(id);
            ra.addFlashAttribute("success", "Đã từ chối công thức.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
