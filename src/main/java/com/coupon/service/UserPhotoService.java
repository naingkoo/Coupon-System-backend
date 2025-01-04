package com.coupon.service;

import com.coupon.entity.UserEntity;
import com.coupon.entity.UserPhotoEntity;
import com.coupon.model.UserPhotoDTO;
import com.coupon.reposistory.UserPhotoRepository;
import com.coupon.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPhotoService {

    private final UserPhotoRepository userPhotoRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserPhotoService(UserPhotoRepository userPhotoRepository, UserRepository userRepository) {
        this.userPhotoRepository = userPhotoRepository;
        this.userRepository = userRepository;
    }

    public UserPhotoDTO saveUserPhoto(UserPhotoDTO userPhotoDTO) {
        // Retrieve the user entity by ID
        UserEntity user = userRepository.findById(userPhotoDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userPhotoDTO.getUser_id()));

        // Create and populate UserPhotoEntity
        UserPhotoEntity userPhotoEntity = new UserPhotoEntity();
        userPhotoEntity.setName(userPhotoDTO.getName());
        userPhotoEntity.setUser(user);

        // Save the entity and retrieve the saved entity
        // Check if the user already has a photo
        UserPhotoEntity existingUserPhoto = userPhotoRepository.findByUserId(user.getId()).orElse(null);

        if (existingUserPhoto != null) {
            // If the user already has a photo, update the existing one
            userPhotoEntity = existingUserPhoto;
            userPhotoEntity.setName(userPhotoDTO.getName());  // Update the photo name with the new one
        } else {
            // If the user does not have a photo, create a new one
            userPhotoEntity = new UserPhotoEntity();
            userPhotoEntity.setName(userPhotoDTO.getName());
            userPhotoEntity.setUser(user);  // Associate the photo with the user
        }

        // Save the entity (either update the existing one or insert a new one)
        UserPhotoEntity savedEntity = userPhotoRepository.save(userPhotoEntity);

        // Map back to DTO
        UserPhotoDTO savedDto = new UserPhotoDTO();
        savedDto.setId(savedEntity.getId());
        savedDto.setName(savedEntity.getName());
        savedDto.setUser_id(savedEntity.getUser().getId());

        return savedDto;
    }
}
