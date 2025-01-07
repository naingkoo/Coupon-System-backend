package com.coupon.controller;

import com.coupon.entity.PaymentMethodEntity;
import com.coupon.model.PaymentMethodDTO;
import com.coupon.reposistory.PaymentMethodRepository;
import com.coupon.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/payment_method")
@CrossOrigin
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PostMapping("/save")
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(
            @RequestPart PaymentMethodDTO paymentMethodDTO,
            @RequestPart MultipartFile image) {
        try {
            System.out.println("Here is payment method successful : " + paymentMethodDTO);

            if (!image.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/QR_images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File file = new File(filePath);
                image.transferTo(file);

                paymentMethodDTO.setImage("/QR_images/" + fileName);  // Ensure the image path is prefixed with `/images/`
            }

            PaymentMethodDTO savedPaymentMethod = paymentMethodService.savePaymentMethod(paymentMethodDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentMethod);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/public/list")
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }
}
