package com.coupon.controller;

import com.coupon.AuthenConfig.JwtService;
import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.UserEntity;
import com.coupon.model.PasswordRequest;
import com.coupon.model.UserDTO;
import com.coupon.model.UserPhotoDTO;
import com.coupon.responObject.HttpResponse;
import com.coupon.service.UserPhotoService;
import com.coupon.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private MyuserDetailService myuserDetailService;

    @GetMapping("/index")
    public String index() {
        return "Hello this is index.";
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity user) {
        user = userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/uploadPhoto")
    public ResponseEntity<UserPhotoDTO> uploadUserPhoto(
            @RequestParam Integer userId,
            @RequestParam MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/users_images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File file = new File(filePath);
                image.transferTo(file);

                UserPhotoDTO userPhotoDTO = new UserPhotoDTO();
                userPhotoDTO.setName("http://localhost:8080/users_images/" + fileName);
                userPhotoDTO.setUser_id(userId);

                UserPhotoDTO savedUserPhoto = userPhotoService.saveUserPhoto(userPhotoDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUserPhoto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/loginUser")
    public HttpResponse<UserDTO> login(@RequestBody UserEntity user) {
        return userService.verify(user);
    }

    @GetMapping("public/list")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/countALlUser")
    public long countALlUser() {
        return userService.countAll();
    }

    @GetMapping("/public/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUserById(id);
        System.out.println("Returning UserDTO: " + userDTO); // Log the returned DTO
        return ResponseEntity.ok(userDTO);
    }

    // Endpoint to update user details
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserDetails(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserDetails(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Integer userId,
                                                 @RequestBody PasswordRequest passwordChangeRequest) {
        // Logic for changing the password
        boolean success = userService.changePassword(userId, passwordChangeRequest.getCurrentPassword(),
                passwordChangeRequest.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Error changing password");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("userId") Integer userId,
            @RequestParam("newPassword") String newPassword) {
        boolean success = userService.resetPassword(userId, newPassword);
        if (success) {
            return ResponseEntity.ok("Password reset successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password.");
        }
    }
}