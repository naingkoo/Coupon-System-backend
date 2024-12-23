package com.coupon.service;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.ReviewEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.ReviewDTO;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.ReviewRepository;
import com.coupon.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository; // Assume userRepository exists

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {

        if (reviewDTO.getUserId() == null) {
            System.out.println("User ID: " + reviewDTO.getUserId());

            throw new IllegalArgumentException("User ID must not be null");
        }

        UserEntity user = userRepository.findById(String.valueOf(reviewDTO.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        // Validate Business ID
        if (reviewDTO.getBusinessId() == null) {
            throw new IllegalArgumentException("Business ID must not be null");
        }

        BusinessEntity business = businessRepository.findById(reviewDTO.getBusinessId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Business ID"));

        // Create and save ReviewEntity
        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setBusinessEntity(business);
        review.setRating(reviewDTO.getRating());
        review.setMessage(reviewDTO.getMessage());
        review.setReview_date(new Date());

        reviewRepository.save(review);

        reviewDTO.setRating(review.getRating());
        reviewDTO.setMessage(review.getMessage());
        reviewDTO.setReview_date(review.getReview_date());
        return reviewDTO;

    }

    public List<ReviewDTO> showAllReviews() {
        // Fetch all ReviewEntities from the database
        List<ReviewEntity> reviews = reviewRepository.findAll();

        // Create a list to store the mapped ReviewDTOs
        List<ReviewDTO> dtoList = new ArrayList<>();

        // Loop through each ReviewEntity and map it to a ReviewDTO
        for (ReviewEntity entity : reviews) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(entity.getId()); // Mapping ID
            dto.setRating(entity.getRating()); // Mapping Rating
            dto.setMessage(entity.getMessage()); // Mapping Message
            dto.setReview_date(entity.getReview_date()); // Mapping Review Date
            dto.setUserId(entity.getUser().getId()); // Mapping User ID
            dto.setBusinessId(entity.getBusinessEntity().getId()); // Mapping Business ID
            // Add the mapped DTO to the list
            dtoList.add(dto);
        }

        // Return the list of ReviewDTOs
        return dtoList;
    }
}

