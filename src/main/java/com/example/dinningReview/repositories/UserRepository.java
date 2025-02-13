package com.example.dinningReview.repositories;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDisplayName(String displayName);
}