package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.PackageDTO;
import com.coupon.model.ReviewDTO;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.ReviewRepository;
import com.coupon.reposistory.UserPhotoRepository;
import com.coupon.reposistory.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository; // Assume userRepository exists

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private ModelMapper mapper;

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
            dto.setUser_name(entity.getUser().getName());
            dto.setBusinessId(entity.getBusinessEntity().getId()); // Mapping Business ID

            // Add the mapped DTO to the list
            dtoList.add(dto);
        }

        // Return the list of ReviewDTOs
        return dtoList;
    }

    // Method to fetch reviews by businessId
    public List<ReviewDTO> showAllReviewsByBusinessId(Integer businessId) {
        // Fetch reviews based on businessId from the repository
        List<ReviewEntity> reviews = reviewRepository.findByBusinessEntityId(businessId);

        // Map each ReviewEntity to ReviewDTO
        List<ReviewDTO> dtoList = new ArrayList<>();
        for (ReviewEntity entity : reviews) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(entity.getId());
            dto.setRating(entity.getRating());
            dto.setMessage(entity.getMessage());
            dto.setReview_date(entity.getReview_date());
            dto.setUserId(entity.getUser().getId());
            dto.setUser_name(entity.getUser().getName());
            dto.setBusinessId(entity.getBusinessEntity().getId());
            dtoList.add(dto);
        }
        return dtoList;
    }
}

