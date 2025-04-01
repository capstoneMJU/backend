package com.example.winterdeom.domain.auth.repository;

import com.example.winterdeom.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
