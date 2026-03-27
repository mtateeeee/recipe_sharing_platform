package com.recipeshare.config;

import com.recipeshare.entity.Chef;
import com.recipeshare.entity.Role;
import com.recipeshare.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ChefRepository chefRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (chefRepository.findByUsername("admin").isEmpty()) {
            Chef admin = Chef.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .displayName("Quản trị viên")
                    .email("admin@recipeshare.local")
                    .role(Role.ADMIN)
                    .build();
            chefRepository.save(admin);
        }
    }
}
