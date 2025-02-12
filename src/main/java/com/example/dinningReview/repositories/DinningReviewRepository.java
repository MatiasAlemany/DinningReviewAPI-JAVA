package com.example.dinningReview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.DinningReview;



public interface DinningReviewRepository extends JpaRepository<DinningReview, Long> {
}