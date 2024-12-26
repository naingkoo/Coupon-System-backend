package com.coupon.controller;


import com.coupon.model.CategoryDTO;
import com.coupon.model.PaymentDTO;
import com.coupon.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/save")
    public ResponseEntity<PaymentDTO> savePayment(@RequestBody PaymentDTO dto) {

        PaymentDTO paymentDTO = paymentService.savePayment(dto);
        return ResponseEntity.ok(paymentDTO);
    }

    @GetMapping("/list")
    public List<PaymentDTO> shwoAllPayment() {
        return paymentService.showAllPayment();
    }
}
