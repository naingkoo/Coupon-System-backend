package com.coupon;

import com.coupon.entity.UserEntity;
import com.coupon.model.UserDTO;
import com.coupon.responObject.HttpResponse;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CouponSaleSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponSaleSystemApplication.class, args);}
	@Bean
	public UserDTO userDTOtoEntity(UserEntity entity) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(entity.getName());
		userDTO.setEmail(entity.getEmail());
		userDTO.setPassword(entity.getPassword());
		userDTO.setPhone(entity.getPhone());
		userDTO.setRole(entity.getRole());
		return userDTO;
	}


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
