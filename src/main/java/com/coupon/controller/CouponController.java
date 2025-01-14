package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.model.CouponDTO;
import com.coupon.model.QRDTO;
import com.coupon.service.CouponService;
import com.coupon.service.QRService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, String>> confirmPurchase(@PathVariable Integer purchaseId) {
        try {
            couponService.confirmPurchaseAndGenerateQR(purchaseId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Purchase confirmed and QR codes generated successfully.");
            return ResponseEntity.ok(response); // Return as a JSON object
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error accepting purchase!");
            return ResponseEntity.badRequest().body(response); // Return as a JSON object
        }
    }

    @PostMapping("/decline/{purchaseId}")
    public ResponseEntity<Map<String, String>> declinePurchase(@PathVariable Integer purchaseId) {
        try {
            couponService.declinePurchaseAndRestoreQuantity(purchaseId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Purchase declined and coupon quantities restored successfully.");
            return ResponseEntity.ok(response); // Return as a JSON object
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error declining purchase!");
            return ResponseEntity.badRequest().body(response); // Return as a JSON object
        }
    }


    @GetMapping("/public/list/{userId}")
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

    @GetMapping("/listbypurchaseId/{purchase_id}")
    public ResponseEntity<List<CouponDTO>> getCouponsByPurchaseId(@PathVariable Integer purchase_id) {
        try {
            List<CouponDTO> coupons = couponService.showCouponbyPurchaseId(purchase_id);

            if (coupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/admin/find")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        try {
            List<CouponDTO> coupons = couponService.findall();
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/public/findbyUserId/{userId}")
    public ResponseEntity<List<CouponDTO>> findAllbyUserId(@PathVariable Integer userId){
        List<CouponDTO> couponList=  couponService.findallByUserId(userId);
        if(couponList.isEmpty()){
            throw new RuntimeException("coupon not found");
        }
        return ResponseEntity.ok(couponList);
    }
    @GetMapping("/report")
    public void downloadCouponReport(
            @RequestParam String fileType, HttpServletResponse response) {
        if ("pdf".equalsIgnoreCase(fileType)) {
            couponService.exportCouponReport(response);
        } else if ("excel".equalsIgnoreCase(fileType)) {
            couponService.exportCouponReportToExcel(response);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }

    @GetMapping("/countALlCoupons")
    public long countALlCoupons(){
        return couponService.countConfirmedCoupons();
    }
}


