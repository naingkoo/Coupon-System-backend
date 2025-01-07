package com.coupon.service;

import com.coupon.entity.QREntity;
import com.coupon.model.QRDTO;
import com.coupon.reposistory.QRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class QRService {

    private final QRRepository qrRepository;

    @Autowired
    public QRService(QRRepository qrRepository) {
        this.qrRepository = qrRepository;
    }

    public QRDTO getQRCodeByCouponId(Integer couponId) {
        // Find QR entity by couponId
        Optional<QREntity> qrEntity = qrRepository.findByCouponId(couponId);

        // If found, return the QR code DTO
        if (qrEntity.isPresent()) {
            QRDTO qrDTO = new QRDTO();
            qrDTO.setId(qrEntity.get().getId());
            qrDTO.setCode(qrEntity.get().getCode()); // Correct method to set the code
            return qrDTO;
        }

        // If not found, throw an exception
        throw new RuntimeException("QR Code not found for Coupon ID: " + couponId);
    }
}
