package com.coupon.controller;

import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.UserEntity;
import com.coupon.model.BusinessDTO;
import com.coupon.model.UserDTO;
import com.coupon.responObject.HttpResponse;
import com.coupon.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
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

    @PostMapping("/loginUser")
    public HttpResponse<UserDTO> login(@RequestBody UserEntity user) {
        return userService.verify(user);
    }

    @GetMapping("public/list")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser();
    }
}