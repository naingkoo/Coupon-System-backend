package com.coupon.service;

import com.coupon.AuthenConfig.JwtService;
import com.coupon.AuthenConfig.MyuserDetailService;
import com.coupon.entity.ReviewEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.ReviewDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
            return httpResponse;
        } else {
            return httpResponse;
        }
    }

    public List<UserDTO> getAllUser() {
        // Fetch all ReviewEntities from the database
        List<UserEntity> users = userRepo.findAll();

        // Create a list to store the mapped ReviewDTOs
        List<UserDTO> dtoList = new ArrayList<>();

        // Loop through each ReviewEntity and map it to a ReviewDTO
        for (UserEntity entity : users) {
            UserDTO dto = new UserDTO();
            dto.setId(entity.getId()); // Mapping ID
            dto.setName(entity.getName()); // Mapping Rating
            dto.setEmail(entity.getEmail()); // Mapping Message
            dto.setPhone(entity.getPhone()); // Mapping Review Date
            dto.setRegister_date(entity.getRegisterDate());
            dto.setRole(entity.getRole());

            dtoList.add(dto);
        }

        // Return the list of ReviewDTOs
        return dtoList;
    }

    public UserDTO getUserById(Integer userId) {
        Optional<UserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userEntity.getId());
            userDTO.setName(userEntity.getName());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setPassword(userEntity.getPassword()); // Be cautious about exposing passwords
            userDTO.setPhone(userEntity.getPhone());
            userDTO.setRole(userEntity.getRole());
            userDTO.setRegister_date(userEntity.getRegisterDate());
            return userDTO;
        } else {
            throw new RuntimeException("User  not found with id: " + userId);
        }
    }

}