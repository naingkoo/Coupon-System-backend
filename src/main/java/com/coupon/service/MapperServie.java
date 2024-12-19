package com.coupon.service;

import com.coupon.entity.UserEntity;
import com.coupon.model.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class MapperServie {
    // Convert UserEntity to UserDTO
    public UserDTO convertToDTO(UserEntity entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(entity.getName());
        userDTO.setEmail(entity.getEmail());
        userDTO.setPassword(entity.getPassword());
        userDTO.setPhone(entity.getPhone());
        userDTO.setRole(entity.getRole());  // Assuming Role is an Enum
        return userDTO;
    }
}