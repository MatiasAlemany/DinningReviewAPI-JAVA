package com.example.dinningReview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dinningReview.repositories.DinningReviewRepository;
import com.example.dinningReview.repositories.RestaurantRepository;
import com.example.dinningReview.repositories.UserRepository;


@RestController
@RequestMapping
public class DinningReviewController{ 
    private final DinningReviewRepository dinningReviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public DinningReviewController (DinningReviewRepository dinningReviewRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.dinningReviewRepository = dinningReviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

}