package com.coupon.service;

import com.coupon.AuthenConfig.JwtService;
import com.coupon.entity.UserEntity;
import com.coupon.entity.UserPhotoEntity;
import com.coupon.model.ReviewDTO;
import com.coupon.model.UserDTO;
import com.coupon.reposistory.UserPhotoRepository;
import com.coupon.reposistory.UserRepository;
import com.coupon.responObject.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserPhotoRepository userPhotoRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.encoder = passwordEncoder;
    }
    public UserEntity addUser(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public HttpResponse<UserDTO> verify(UserEntity user) {

        HttpResponse<UserDTO> httpResponse= new HttpResponse<UserDTO>();
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            //  return jwtService.generateToken()  ;
            UserEntity correctUser=userRepo.findByEmail(user.getEmail()).orElseThrow(()-> new UsernameNotFoundException("username not found!"));
            UserDTO userDTO = new UserDTO();
            userDTO.setName(correctUser.getName());
            userDTO.setEmail(correctUser.getEmail());
            userDTO.setPhone(correctUser.getPhone());
            userDTO.setRole(correctUser.getRole());
            userDTO.setToken( jwtService.generateToken(correctUser));// Assuming Role is an Enum
            httpResponse.getDate();
            httpResponse.setData(userDTO);
            return httpResponse;
        } else {
            return httpResponse;
        }
    }

    public List<UserDTO> getAllUser() {
        // Fetch all UserEntities from the database
        List<UserEntity> users = userRepo.findAll();

        // Create a list to store the mapped UserDTOs
        List<UserDTO> dtoList = new ArrayList<>();

        // Loop through each UserEntity and map it to a UserDTO
        for (UserEntity entity : users) {
            UserDTO dto = new UserDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setEmail(entity.getEmail());
            dto.setPhone(entity.getPhone());
            dto.setRegister_date(entity.getRegisterDate());
            dto.setRole(entity.getRole());

            // Fetch the user photo by user ID
            Optional<UserPhotoEntity> userPhotoOptional = userPhotoRepo.findByUserId(entity.getId());
            if (userPhotoOptional.isPresent()) {
                String photoName = userPhotoOptional.get().getName();
                dto.setPhoto(photoName); // Assuming 'getName()' returns the photo file name
            } else {
                dto.setPhoto(null); // No photo available
            }
            // Add the UserDTO to the list
            dtoList.add(dto);
        }
        // Return the list of UserDTOs
        return dtoList;
    }

    public long countAll(){
        return userRepo.countByIsdeleteFalse();
    }


    public UserDTO getUserById(Integer userId) {
        Optional<UserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();

            // Fetch user photo by user ID
            Optional<UserPhotoEntity> userPhotoOptional = userPhotoRepo.findByUserId(userId);

            // Map UserEntity to UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userEntity.getId());
            userDTO.setName(userEntity.getName());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setPassword(userEntity.getPassword()); // Be cautious about exposing passwords
            userDTO.setPhone(userEntity.getPhone());
            userDTO.setRole(userEntity.getRole());
            userDTO.setRegister_date(userEntity.getRegisterDate());

            // Log if photo is found
            if (userPhotoOptional.isPresent()) {
                String photoName = userPhotoOptional.get().getName();
                userDTO.setPhoto(photoName); // Assuming 'getName()' returns the photo file name
                System.out.println("User Photo: " + photoName); // Log the photo name
            } else {
                userDTO.setPhoto(null); // No photo available
                System.out.println("No photo found for user ID: " + userId); // Log if no photo is found
            }

            return userDTO;
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public UserDTO updateUserDetails(Integer id, UserDTO userDTO) {
        // Check if user exists
        UserEntity user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return null; // User not found
        }

        // Update user details
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());

        // Save updated user to the database
        UserEntity updatedUser = userRepo.save(user);

        // Create a new UserDTO and set values using setters
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(updatedUser.getId());
        updatedUserDTO.setName(updatedUser.getName());
        updatedUserDTO.setPhone(updatedUser.getPhone());
        updatedUserDTO.setEmail(updatedUser.getEmail());

        return updatedUserDTO;
    }

    // UserService.java
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        // Find user by ID
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return false;  // User not found
        }

        // Check if current password matches the user's current password
        if (!encoder.matches(currentPassword, user.getPassword())) {
            return false;  // Current password is incorrect
        }

        // Encrypt the new password
        String encryptedPassword = encoder.encode(newPassword);
        // Update the user's password
        user.setPassword(encryptedPassword);
        userRepo.save(user);

        return true;  // Password changed successfully
    }

    public boolean resetPassword(Integer userId, String newPassword) {
        // Find user by ID
        Optional<UserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Encrypt the new password
            String encryptedPassword = encoder.encode(newPassword);

            // Update the user's password
            user.setPassword(encryptedPassword);

            // Save the updated user
            userRepo.save(user);

            return true; // Password changed successfully
        } else {
            return false; // User not found
        }
    }


    public UserEntity findByEmail(String email) {
        Optional<UserEntity> user =this.userRepo.findByEmail(email);
        return user.orElseThrow(()->new UsernameNotFoundException("email not found"));
    }

    public boolean updatePassword(String email, String newPassword) {
        // Encrypt the new password
        String encodedPassword = encoder.encode(newPassword);

        // Perform the update on the password field only
        int rowsAffected = userRepo.updatePasswordByEmail(email, encodedPassword);

        // Return true if at least one row was updated, false otherwise
        return rowsAffected > 0;
    }
}