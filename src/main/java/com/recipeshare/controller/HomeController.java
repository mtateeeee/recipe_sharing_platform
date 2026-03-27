package com.recipeshare.controller;

import com.recipeshare.entity.RecipeStatus;
import com.recipeshare.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RecipeService recipeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recipes", recipeService.findApprovedDtos(PageRequest.of(0, 12)));
        return "index";
    }
}
