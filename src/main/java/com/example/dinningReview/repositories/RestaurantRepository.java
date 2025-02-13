package com.example.dinningReview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dinningReview.entities.Restaurant;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
    Optional<Restaurant> findById(Long id);
}