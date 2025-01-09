package com.coupon.controller;

import com.coupon.model.TransferDTO;
import com.coupon.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/public/transferlist/{sender_id}")
    public ResponseEntity<List<TransferDTO>> showTransferCouponList(@PathVariable("sender_id") Integer sender_id) {
        try {
            List<TransferDTO> transferDTOList = transferService.showTransferCouponList(sender_id);
            return ResponseEntity.ok(transferDTOList);
        } catch (RuntimeException e) {
            // Handle exception (user not found, etc.)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/public/receivelist/{receiver_id}")
    public ResponseEntity<List<TransferDTO>> showCouponByReceiverId(@PathVariable("receiver_id") Integer receiver_id) {
        try {
            List<TransferDTO> transferDTOList = transferService.showCouponByReceiverId(receiver_id);
            return ResponseEntity.ok(transferDTOList);
        } catch (RuntimeException e) {
            // Handle exception (user not found, etc.)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
