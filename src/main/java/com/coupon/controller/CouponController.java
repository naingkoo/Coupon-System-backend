package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.model.CouponDTO;
import com.coupon.model.QRDTO;
import com.coupon.service.CouponService;
import com.coupon.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private QRService qrService;

    @PostMapping("/accept/{purchaseId}")
    public ResponseEntity<String> confirmPurchase(@PathVariable Integer purchaseId) {
        try {
            couponService.confirmPurchaseAndGenerateQR(purchaseId);
            return ResponseEntity.ok("Purchase confirmed and QR codes generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/list/{userId}")
    public ResponseEntity<List<CouponDTO>> shwoAllCoupon(@PathVariable Integer userId) {
        System.out.println("userId" + userId);
        List<CouponDTO> coupons = couponService.showCouponbyUserId(userId);

        if(coupons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coupons);
}

    @GetMapping("/code/{id}")
    public ResponseEntity<QRDTO> getQRCode(@PathVariable("id") Integer id) {
        QRDTO qrDto = qrService.getQRCodeByCouponId(id);
        return ResponseEntity.ok(qrDto);
    }
}


