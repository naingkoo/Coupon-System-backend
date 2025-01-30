package com.coupon.controller;

import com.coupon.model.PurchaseDTO;
import com.coupon.model.PurchaseRequest;
import com.coupon.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/save")
    public ResponseEntity<String> savePurchase(@RequestBody PurchaseRequest purchaseRequest) {
        try {
            purchaseService.savePurchaseAndCoupons(
                    purchaseRequest.getPurchaseDTO(),
                    purchaseRequest.getSelectedPackages()
            );
            return ResponseEntity.ok("Purchase and coupons saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save purchase: " + e.getMessage());
        }
    }

    @GetMapping("/public/listALL")
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseService.getAllPurchasesWithUserDetails();
    }

    @GetMapping("/public/list/{user_id}")
    public ResponseEntity<List<PurchaseDTO>> getPurchasesByUserId(@PathVariable Integer user_id) {
        List<PurchaseDTO> purchases = purchaseService.getPurchasesByUserId(user_id);

        if(purchases.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/payment-type-distribution")
    public ResponseEntity<Map<String, Integer>> getPaymentTypeDistribution() {
        Map<String, Integer> distribution = purchaseService.getPaymentTypeDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/countConfirmed")
    public long countConfirmedPurchases() {
        return purchaseService.getConfirmedPurchasesCount();
    }

    @GetMapping("/count-pending")
    public long getPendingPurchaseCount() {
        return purchaseService.countPendingPurchases();
    }
}

