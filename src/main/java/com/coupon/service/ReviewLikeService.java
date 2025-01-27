package com.coupon.service;

import com.coupon.entity.ReviewAction;
import com.coupon.entity.ReviewEntity;
import com.coupon.entity.ReviewLikeEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.ReviewLikeDTO;
import com.coupon.reposistory.ReviewLikeRepository;
import com.coupon.reposistory.ReviewRepository;
import com.coupon.reposistory.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReviewLikeService {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveOrUpdateReviewAction(ReviewLikeDTO reviewLikeDTO) {
        // Validate action
        ReviewAction action;
        try {
            action = ReviewAction.valueOf(reviewLikeDTO.getAction().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action: " + reviewLikeDTO.getAction());
        }

        // Check if a record exists for the given review and user
        Optional<ReviewLikeEntity> optionalEntity = reviewLikeRepository
                .findByReviewIdAndUserId(reviewLikeDTO.getReviewId(), reviewLikeDTO.getUserId());

        ReviewLikeEntity entity;
        if (optionalEntity.isPresent()) {
            // Update existing entity
            entity = optionalEntity.get();
            entity.setAction(action);
        } else {
            // Create new entity
            entity = new ReviewLikeEntity();

            // Map review and user relationships
            ReviewEntity review = reviewRepository.findById(reviewLikeDTO.getReviewId())
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewLikeDTO.getReviewId()));

            UserEntity user = userRepository.findById(reviewLikeDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + reviewLikeDTO.getUserId()));

            entity.setReview(review);
            entity.setUser(user);
            entity.setAction(action);
        }

        // Log and save the entity
        System.out.println("Saving entity: " + entity);
        reviewLikeRepository.save(entity);
    }

}


