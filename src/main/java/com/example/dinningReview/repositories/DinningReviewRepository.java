package com.example.dinningReview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.DinningReview;
import java.util.List;



public interface DinningReviewRepository extends JpaRepository<DinningReview, Long> {
        List<DinningReview> findAllByRestaurantId(Long id);
}