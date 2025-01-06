package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.model.CouponDTO;
import com.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/list")
    public List<CouponDTO> shwoAllCoupon() {
        return copuonService.showAllCoupon();
    }
}
