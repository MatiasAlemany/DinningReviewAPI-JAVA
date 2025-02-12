package com.example.dinningReview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dinningReview.entities.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

}