package com.example.dinningReview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    /* LLAMAR A TODAS LAS RESEÑAS */
    @GetMapping
    public List<DinningReview> getDinningReview() {
        return dinningReviewRepository.findAll();
    }

    /* CREAR RESTAURANTE */
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
    }

    /* VER TODOS LOS RESTAURANTES */
    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants(){
        return restaurantRepository.findAll();
    }

    /* POSTEAR RESEÑA NUEVA */
    @PostMapping("/{restaurantId}")
    public ResponseEntity<DinningReview> createReview(@RequestBody DinningReview review, @PathVariable Long restaurantId) {

        String displayName = review.getUser();
        Optional<Restaurant> restaurantValidation = restaurantRepository.findById(restaurantId);
        Optional<User> userValidation = userRepository.findByDisplayName(displayName);

        if (!restaurantValidation.isPresent() || !userValidation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        if (!isValidScore(review.getPeanutScore()) ||
                !isValidScore(review.getEggScore()) ||
                !isValidScore(review.getDairyScore())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        // Guardar la reseña si es válida

        review.setRestaurant(restaurantValidation.get());
        DinningReview savedReview = dinningReviewRepository.save(review);
        
        // Calcular el nuevo overallScore
        Restaurant restaurant = restaurantValidation.get();
        Double newOverallScore = calculateOverallScore(restaurant);

        // Actualizar el restaurant con el nuevo overallScore
        restaurant.setOverallScore(newOverallScore);
        restaurantRepository.save(restaurant);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    private boolean isValidScore(Integer score) {
        return score == null || (score >= 1 && score <= 5);
    }

    // Método para calcular el promedio de los puntajes
    private Double calculateOverallScore(Restaurant restaurant) {
    List<DinningReview> reviews = dinningReviewRepository.findAllByRestaurant(restaurant);
    double totalScore = 0;
    int reviewCount = reviews.size();

    for (DinningReview review : reviews) {
        totalScore += review.getPeanutScore() + review.getEggScore() + review.getDairyScore();
    }

    // Calcular el promedio si hay reseñas
    return reviewCount > 0 ? totalScore / (reviewCount * 3) : 0; 
}

    /* VER TODAS LAS RESEÑAS DE UN RESTAURANTE */
    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<DinningReview>> restaurantReviews(@PathVariable Long restaurantId){
        Optional<Restaurant> restaurantValidation = restaurantRepository.findById(restaurantId);

        if(!restaurantValidation.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<DinningReview> allReviews = dinningReviewRepository.findAllByRestaurant_Id(restaurantId);
        return ResponseEntity.ok(allReviews);
    }

    /* CREAR USUARIO */
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /* ELIMINAR RESTAURANTE */
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId){
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        if (!restaurant.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }  
        
        restaurantRepository.deleteById(restaurantId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /* ELIMINAR USUARIO */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        Optional<Restaurant> restaurant = restaurantRepository.findById(userId);

        if (!restaurant.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }  
        
        restaurantRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

     /* ELIMINAR REVIEW */
     @DeleteMapping("/{reviewId}")
     public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId){
         Optional<Restaurant> restaurant = restaurantRepository.findById(reviewId);
 
         if (!restaurant.isPresent()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
         }  
         
         restaurantRepository.deleteById(reviewId);
         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
     }
}
