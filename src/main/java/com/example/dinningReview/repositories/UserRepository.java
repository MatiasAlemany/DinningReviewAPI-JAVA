package com.example.dinningReview.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNameOptional(String displayName);
    List<User> findByUserName(String displayName);
}