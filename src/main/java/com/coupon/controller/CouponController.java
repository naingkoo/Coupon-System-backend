package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.model.CouponDTO;
import com.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@CrossOrigin
public class CouponController {

    @Autowired
    private CouponService copuonService;

//    @PostMapping("/save")
//    public CouponDTO saveCoupon(@RequestBody CouponDTO dto){
//        return copuonService.saveCoupon(dto);
//
//    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<CouponDTO>> shwoAllCoupon(@PathVariable Integer userId) {
        System.out.println("userId" + userId);
        List<CouponDTO> coupons = copuonService.showCouponbyUserId(userId);

        if(coupons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coupons);
    }
}
