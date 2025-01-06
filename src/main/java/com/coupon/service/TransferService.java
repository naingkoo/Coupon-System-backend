package com.coupon.service;

import com.coupon.entity.CouponEntity;
import com.coupon.entity.TransferEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.TransferDTO;
import com.coupon.reposistory.CouponRepository;
import com.coupon.reposistory.TransferRepository;
import com.coupon.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepo;

    @Autowired
    private CouponRepository couponRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public TransferDTO transferCoupon(Integer sender_id, String receiverEmail, Integer coupon_id) {

        CouponEntity couponEntity = couponRepo.findById(coupon_id)
                .orElseThrow(() ->  new RuntimeException("Coupon not found with id: " + coupon_id));

        UserEntity sender = userRepo.findById(sender_id)
                .orElseThrow(() -> new RuntimeException("Sender user not found with ID: " + sender_id));

        UserEntity receiver = userRepo.findByEmail(receiverEmail)
                .orElseThrow(() ->  new RuntimeException("Receiver user not found with email: " + receiverEmail));
        Integer receiverId = receiver.getId();

        if (couponEntity.getStatus() == false) {
            throw new IllegalStateException("Coupon is already transferred.");
        }

        couponEntity.setTransfer_status(false);
        couponRepo.save(couponEntity);

        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setUser(sender);
        transferEntity.setCoupon(couponEntity);
        transferEntity.setReceiver_id(receiverId);
        transferEntity.setTransfer_date(new Date());
        transferEntity.setStatus(true);

        transferEntity = transferRepo.save(transferEntity);

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transferEntity.getId());
        transferDTO.setTransfer_date(transferEntity.getTransfer_date());
        transferDTO.setStatus(transferEntity.getStatus());
        transferDTO.setSender_id(transferEntity.getUser().getId());
        transferDTO.setCoupon_id(transferEntity.getCoupon().getId());
        transferDTO.setReceiver_id(transferEntity.getReceiver_id());

        return transferDTO;
    }
}
