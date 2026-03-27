package com.recipeshare.security;

import com.recipeshare.entity.Chef;
import com.recipeshare.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ChefRepository chefRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Chef chef = chefRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(
                chef.getUsername(),
                chef.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + chef.getRole().name()))
        );
    }
}
