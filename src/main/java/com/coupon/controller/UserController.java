package com.coupon.controller;

import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.UserEntity;
import com.coupon.model.UserDTO;
import com.coupon.responObject.HttpResponse;
import com.coupon.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UserEntity addUser(@RequestBody UserEntity user) {
        return userService.addUser(user);
    }


    @PostMapping("/loginUser")
    public HttpResponse<UserDTO> login(@RequestBody UserEntity user) {
        return userService.verify(user);
    }


    @GetMapping("/display")
    public String display() {
        return "Hello this is display.";
    }
}