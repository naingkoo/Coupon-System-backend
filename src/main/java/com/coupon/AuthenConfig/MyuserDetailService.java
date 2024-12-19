package com.coupon.AuthenConfig;

import com.coupon.entity.UserEntity;
import com.coupon.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyuserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRe;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user=userRe.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("username not found!"));
    System.out.println("hello "+ user);
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }



}
