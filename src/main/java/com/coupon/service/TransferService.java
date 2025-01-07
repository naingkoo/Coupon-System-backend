package com.coupon.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.coupon.entity.CouponEntity;
import com.coupon.entity.PackageEntity;
import com.coupon.entity.TransferEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.TransferDTO;
import com.coupon.reposistory.CouponRepository;
import com.coupon.reposistory.PackageRepository;
import com.coupon.reposistory.TransferRepository;
import com.coupon.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepo;

    @Autowired
    private CouponRepository couponRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PackageRepository packageRepo;

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

    public List<TransferDTO> showTransferCouponList(Integer sender_id) {

        List<TransferEntity> transferEntities = transferRepo.findByUserId(sender_id);

        List<TransferDTO> transferDTOList = new ArrayList<>();
        for(TransferEntity transferEntity: transferEntities) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setId(transferEntity.getId());
            transferDTO.setTransfer_date(transferEntity.getTransfer_date());


            if(transferEntity.getCoupon() != null && transferEntity.getCoupon().getPackageEntity() != null) {

                transferDTO.setPackageName(transferEntity.getCoupon().getPackageEntity().getName());
                transferDTO.setExpired_date(transferEntity.getCoupon().getPackageEntity().getExpired_date());
                transferDTO.setImage(transferEntity.getCoupon().getPackageEntity().getImage());
            }

            UserEntity receiver = userRepo.findById(transferEntity.getReceiver_id())
                    .orElseThrow(() -> new RuntimeException("Receiver user not found with ID: " + transferEntity.getReceiver_id()));

            transferDTO.setReceiverName(receiver.getName());

            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }


    public List<TransferDTO> showCouponByReceiverId(Integer receiver_id) {

        List<TransferEntity> transferEntities = transferRepo.findByReceiverId(receiver_id);

        List<TransferDTO> transferDTOList = new ArrayList<>();
        for(TransferEntity transferEntity: transferEntities) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setId(transferEntity.getId());
            transferDTO.setTransfer_date(transferEntity.getTransfer_date());

            if(transferEntity.getCoupon() != null && transferEntity.getCoupon().getPackageEntity() != null) {

                transferDTO.setPackageName(transferEntity.getCoupon().getPackageEntity().getName());
                transferDTO.setExpired_date(transferEntity.getCoupon().getPackageEntity().getExpired_date());
                transferDTO.setImage(transferEntity.getCoupon().getPackageEntity().getImage());
            }

            UserEntity sender = transferEntity.getUser();
            transferDTO.setSenderName(sender.getName());

            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }




}
