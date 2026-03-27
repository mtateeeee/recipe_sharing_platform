package com.recipeshare.repository;

import com.recipeshare.entity.Chef;
import com.recipeshare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChefRepository extends JpaRepository<Chef, Long> {

    Optional<Chef> findByUsername(String username);

    Optional<Chef> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Chef> findByRole(Role role);
}
