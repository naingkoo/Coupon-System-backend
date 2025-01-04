package com.coupon.AuthenConfig;
import com.coupon.entity.UserEntity;
import com.coupon.model.UserPhotoDTO;
import com.coupon.reposistory.UserRepository;
import com.coupon.service.UserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPhotoService userPhotoService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("User attributes: " + oAuth2User.getAttributes());
        // Extract user details
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = (String) oAuth2User.getAttribute("picture");

        // Save or update user in your database
        Optional<UserEntity> user = userRepository.findByEmail(email);
        user.orElseGet(() -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setName(name);
            userEntity.setRole("CUSTOMER");
            userEntity.setPassword("");
           userEntity= userRepository.save(userEntity);
            UserPhotoDTO userPhotoDTO = new UserPhotoDTO();
            userPhotoDTO.setName(picture);
            userPhotoDTO.setUser_id(userEntity.getId());
            userPhotoService.saveUserPhoto(userPhotoDTO);
            return userEntity;
        });


        return oAuth2User;
    }

}
