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
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
