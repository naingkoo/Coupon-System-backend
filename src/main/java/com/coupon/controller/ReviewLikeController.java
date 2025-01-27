package com.coupon.controller;

import com.coupon.model.ReviewLikeDTO;
import com.coupon.service.ReviewLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
public class ReviewLikeController {

    @Autowired
    private ReviewLikeService reviewLikeService;

    @PostMapping("/action/{reviewId}")
    public ResponseEntity<String> updateReviewAction(
            @PathVariable("reviewId") Integer reviewId,
            @RequestParam String action,
            @RequestParam Integer userId) {

        ReviewLikeDTO reviewLikeDTO = new ReviewLikeDTO();
        reviewLikeDTO.setReviewId(reviewId);
        reviewLikeDTO.setUserId(userId);
        reviewLikeDTO.setAction(action);

        try {
            reviewLikeService.saveOrUpdateReviewAction(reviewLikeDTO);
            return ResponseEntity.ok("{\"message\":\"Action saved successfully\"}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error saving review action: " + e.getMessage() + "\"}");
        }
    }

}


