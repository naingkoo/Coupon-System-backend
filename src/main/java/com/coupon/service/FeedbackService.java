package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.FeedbackDTO;
import com.coupon.model.ReviewDTO;
import com.coupon.reposistory.FeedbackRepository;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.UserRepository;
import com.coupon.reposistory.UserPhotoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository; // Assume userRepository exists

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private ModelMapper mapper;

    public FeedbackDTO saveFeedback(FeedbackDTO reviewDTO) {

        if (reviewDTO.getUserId() == null) {
            System.out.println("User ID: " + reviewDTO.getUserId());

            throw new IllegalArgumentException("User ID must not be null");
        }

        UserEntity user = userRepository.findById(String.valueOf(reviewDTO.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));


        // Create and save ReviewEntity
        FeedbackEntity review = new FeedbackEntity();
        review.setUser(user);
        review.setMessage(reviewDTO.getMessage());
        review.setFeedback_date(new Date());

        feedbackRepository.save(review);
//        reviewDTO.setUserId(reviewDTO.getUserId());
        reviewDTO.setUser_name(reviewDTO.getUser_name());
        reviewDTO.setUser_email(reviewDTO.getUser_email());
        reviewDTO.setMessage(review.getMessage());
        reviewDTO.setFeedback_date(review.getFeedback_date());
        return reviewDTO;

    }

    public List<FeedbackDTO> showAllFeedback() {
        // Fetch all FeedbackEntities from the database
        List<FeedbackEntity> feedbackList = feedbackRepository.findAll();

        // Create a list to store the mapped FeedbackDTOs
        List<FeedbackDTO> dtoList = new ArrayList<>();

        // Loop through each FeedbackEntity and map it to a FeedbackDTO
        for (FeedbackEntity entity : feedbackList) {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setId(entity.getId());
            dto.setMessage(entity.getMessage());
            dto.setFeedback_date(entity.getFeedback_date());

            UserEntity user = entity.getUser();
            dto.setUserId(user.getId());
            dto.setUser_name(user.getName());
            dto.setUser_email(user.getEmail());

            // Get the first photo for the user
            Optional<UserPhotoEntity> userPhoto = userPhotoRepository.findPhotosByUserId(user.getId()).stream().findFirst();
            dto.setUser_photo(userPhoto.map(UserPhotoEntity::getName).orElse(null));

            // Add the mapped DTO to the list
            dtoList.add(dto);
        }

        // Return the list of FeedbackDTOs
        return dtoList;
    }
}
