package com.example.dinningReview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dinningReview.repositories.DinningReviewRepository;
import com.example.dinningReview.repositories.RestaurantRepository;
import com.example.dinningReview.repositories.UserRepository;
import com.example.dinningReview.entities.DinningReview;
import java.util.List;
import java.util.Optional;
import com.example.dinningReview.entities.Restaurant;
import com.example.dinningReview.entities.User;

@RestController
@RequestMapping("/reviews")
public class DinningReviewController {
    private final DinningReviewRepository dinningReviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public DinningReviewController(DinningReviewRepository dinningReviewRepository,
            RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.dinningReviewRepository = dinningReviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<DinningReview> getDinningReview() {
        return dinningReviewRepository.findAll();
    }

    @PostMapping("/{restaurantId}")
    public ResponseEntity<DinningReview> createReview(@RequestBody DinningReview review, @PathVariable Long restaurantId, String displayName) {
        Optional<Restaurant> restaurantValidation = restaurantRepository.findById(restaurantId);
        Optional<User> userValidation = userRepository.findByDisplayName(displayName);

        if (!restaurantValidation.isPresent() || !userValidation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!isValidScore(review.getPeanutScore()) ||
                !isValidScore(review.getEggScore()) ||
                !isValidScore(review.getDairyScore())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        // Guardar la reseña si es válida
        review.setRestaurantId(restaurantValidation.get());
        DinningReview savedReview = dinningReviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    private boolean isValidScore(Integer score) {
        return score == null || (score >= 1 && score <= 5);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<DinningReview>> restaurantReviews(@PathVariable Long restaurantId){
        Optional<Restaurant> restaurantValidation = restaurantRepository.findById(restaurantId);

        if(!restaurantValidation.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<DinningReview> allReviews = dinningReviewRepository.findAllByRestaurantId(restaurantId);
        return ResponseEntity.ok(allReviews);
    }
}
