package com.coupon.service;

import com.coupon.AuthenConfig.JwtService;
import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.UserEntity;
import com.coupon.model.UserDTO;
import com.coupon.reposistory.UserRepository;
import com.coupon.responObject.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final PasswordEncoder encoder;


    @Autowired
   private UserRepository userRepo;

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

         //
            return httpResponse;
        } else {
            return httpResponse;
        }
    }

}