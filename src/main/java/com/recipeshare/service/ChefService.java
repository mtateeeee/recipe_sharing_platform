package com.recipeshare.service;

import com.recipeshare.dto.ChefDto;
import com.recipeshare.entity.Chef;
import com.recipeshare.entity.Role;
import com.recipeshare.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChefService {

    private final ChefRepository chefRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Chef register(String username, String password, String displayName, String email, Role role) {
        if (chefRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (email != null && !email.isBlank() && chefRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        Chef chef = Chef.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .displayName(displayName != null ? displayName : username)
                .email(email)
                .role(role != null ? role : Role.VIEWER)
                .build();
        return chefRepository.save(chef);
    }

    public ChefDto toDto(Chef chef) {
        if (chef == null) return null;
        return ChefDto.builder()
                .id(chef.getId())
                .username(chef.getUsername())
                .displayName(chef.getDisplayName())
                .email(chef.getEmail())
                .avatarUrl(chef.getAvatarUrl())
                .role(chef.getRole())
                .build();
    }

    public Chef findByUsername(String username) {
        return chefRepository.findByUsername(username).orElse(null);
    }

    public Chef findById(Long id) {
        return chefRepository.findById(id).orElse(null);
    }
}
