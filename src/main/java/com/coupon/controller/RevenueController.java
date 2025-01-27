package com.coupon.controller;

import com.coupon.model.PaidCouponDTO;
import com.coupon.model.RevenueDTO;
import com.coupon.service.CartService;
import com.coupon.service.PaidCouponService;
import com.coupon.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private PaidCouponService paidCouponService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRevenue(@RequestBody RevenueDTO revenueDTO) {
        try {
            revenueService.saveRevenue(revenueDTO);
            return ResponseEntity.ok("Revenue and paid coupons saved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/ListAll")
    public List<RevenueDTO> getAllRevenues() {
        return revenueService.getAllRevenueWithBusinessName();
    }

    @GetMapping("/paidList/{revenueId}")
    public ResponseEntity<List<PaidCouponDTO>> getPaidCouponsByRevenueId(@PathVariable Integer revenueId) {
        List<PaidCouponDTO> paidCoupons = paidCouponService.getCouponsByRevenueId(revenueId);
        return ResponseEntity.ok(paidCoupons);
    }

    @GetMapping("/byBusiness/{businessId}")
    public List<RevenueDTO> getRevenuesByBusinessId(@PathVariable Integer businessId) {
        return revenueService.getRevenuesByBusinessId(businessId);
    }
}
