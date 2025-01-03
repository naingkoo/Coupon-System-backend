package com.coupon.controller;

import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.UserEntity;
import com.coupon.model.BusinessDTO;
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
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

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
       user= userService.addUser(user);
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
                userPhotoDTO.setName("/users_images/" + fileName);
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

    @GetMapping("/public/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUserById(id);
        System.out.println("Returning UserDTO: " + userDTO); // Log the returned DTO
        return ResponseEntity.ok(userDTO);
    }


}