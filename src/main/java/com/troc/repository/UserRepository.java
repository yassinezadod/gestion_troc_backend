package com.troc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.troc.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}