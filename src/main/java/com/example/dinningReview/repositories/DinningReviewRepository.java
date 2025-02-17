package com.example.dinningReview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.DinningReview;
import com.example.dinningReview.entities.Restaurant;

import java.util.List;



public interface DinningReviewRepository extends JpaRepository<DinningReview, Long> {
        List<DinningReview> findAllByRestaurant_Id(Long id);
        List<DinningReview> findAllByRestaurant(Restaurant restaurant);
}