package com.coupon.controller;

import com.coupon.model.ReviewDTO;
import com.coupon.service.BusinessService;
import com.coupon.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Update with your Angular app URL
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.saveReview(reviewDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Review saved successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/public/list")
    public List<ReviewDTO> getAllReviews() {
        return reviewService.showAllReviews();
    }

    @GetMapping("/business/{businessId}")
    public List<ReviewDTO> showAllReviewsByBusinessId(@PathVariable("businessId") Integer businessId) {
        return reviewService.showAllReviewsByBusinessId(businessId);
    }
}
