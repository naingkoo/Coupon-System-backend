package com.coupon.controller;

import com.coupon.model.TransferDTO;
import com.coupon.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferDTO> transferCoupon(@RequestBody TransferDTO dto) {
        try {
            TransferDTO transferDTO = transferService.transferCoupon(dto.getSender_id(), dto.getReceiverEmail(), dto.getCoupon_id());
            return ResponseEntity.ok(transferDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // or provide a detailed message
        }
    }

}
