package com.recipeshare.controller;

import com.recipeshare.entity.Chef;
import com.recipeshare.entity.Role;
import com.recipeshare.security.JwtUtil;
import com.recipeshare.service.ChefService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final ChefService chefService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "VIEWER") String role,
            RedirectAttributes ra,
            HttpServletResponse response) throws IOException {
        try {
            Role r = Role.valueOf(role.toUpperCase());
            if (r == Role.ADMIN) r = Role.VIEWER;
            Chef chef = chefService.register(username, password, displayName, email, r);
            String token = jwtUtil.generateToken(chef.getUsername(), chef.getRole().name());
            var cookie = new jakarta.servlet.http.Cookie("jwt", token);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            ra.addFlashAttribute("success", "Đăng ký thành công!");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
