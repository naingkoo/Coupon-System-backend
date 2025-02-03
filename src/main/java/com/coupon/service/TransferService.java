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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TransferService {
@Autowired
private NotificationService notificationService;

    @Autowired
    private TransferRepository transferRepo;

    @Autowired
    private CouponRepository couponRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PackageRepository packageRepo;


    @Transactional
    public TransferDTO transferCoupon(Integer sender_id, String receiverEmail, Integer coupon_id,String note) {

        // Fetch the coupon
        CouponEntity couponEntity = couponRepo.findById(coupon_id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + coupon_id));

        // Fetch the sender
        UserEntity sender = userRepo.findById(sender_id)
                .orElseThrow(() -> new RuntimeException("Sender user not found with ID: " + sender_id));

        // Fetch the receiver
        UserEntity receiver = userRepo.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver user not found with email: " + receiverEmail));

        // Validate coupon status
        if (!couponEntity.getUsed_status()) {
            throw new IllegalStateException("Coupon is already transferred or used.");
        }

        // Update coupon transfer status
        couponEntity.setTransfer_status(false);
        couponRepo.save(couponEntity);

        // Create a new transfer record
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setUser(sender);
        transferEntity.setCoupon(couponEntity);
        transferEntity.setReceiver_id(receiver.getId());
        transferEntity.setTransfer_date(new Date());
        transferEntity.setStatus(true);
        transferEntity.setNote(note);

        // Save transfer entity
        transferEntity = transferRepo.save(transferEntity);

        // Prepare TransferDTO
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transferEntity.getId());
        transferDTO.setTransfer_date(transferEntity.getTransfer_date());
        transferDTO.setStatus(transferEntity.getStatus());
        transferDTO.setNote(transferEntity.getNote());
        transferDTO.setSender_id(transferEntity.getUser().getId());
        transferDTO.setCoupon_id(transferEntity.getCoupon().getId());
        transferDTO.setReceiver_id(transferEntity.getReceiver_id());

        // Send a notification to the receiver
        String receiverIdString = receiver.getId().toString();
        String senderName = sender.getName();

        UserEntity receiverId= new UserEntity();
        receiverId.setId(receiver.getId());
        notificationService.sendTextNotification("Transfer Coupon","You have received a new coupon from " + senderName, receiverId);

        System.out.println("Transfer completed successfully for receiver ID: " + receiverIdString);

        return transferDTO;
    }


    public List<TransferDTO> showTransferCouponList(Integer sender_id) {

        List<TransferEntity> transferEntities = transferRepo.findByUserId(sender_id);

        List<TransferDTO> transferDTOList = new ArrayList<>();
        for(TransferEntity transferEntity: transferEntities) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setId(transferEntity.getId());
            transferDTO.setTransfer_date(transferEntity.getTransfer_date());
            transferDTO.setNote(transferEntity.getNote());


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
            transferDTO.setNote(transferEntity.getNote());

            if(transferEntity.getCoupon() != null && transferEntity.getCoupon().getPackageEntity() != null) {

                transferDTO.setPackageName(transferEntity.getCoupon().getPackageEntity().getName());
                transferDTO.setExpired_date(transferEntity.getCoupon().getPackageEntity().getExpired_date());
                transferDTO.setImage(transferEntity.getCoupon().getPackageEntity().getImage());
                transferDTO.setCoupon_id(transferEntity.getCoupon().getId());
            }

            UserEntity sender = transferEntity.getUser();
            transferDTO.setSenderName(sender.getName());

            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }


    public List<TransferDTO> showUsedCouponbyreceiverId(Integer receiverId) {

        List<TransferEntity> coupon = transferRepo.findUsedCouponsByReceiverId(receiverId);

        List<TransferDTO> dtoList = new ArrayList<>();
        for(TransferEntity entity: coupon) {

            TransferDTO dto = new TransferDTO();
            dto.setId(entity.getId());
            dto.setImage(entity.getCoupon().getPackageEntity().getImage());
            dto.setPackageName(entity.getCoupon().getPackageEntity().getName());
            dto.setUsed_date(entity.getCoupon().getIsUsed().getUsed_date());
            dto.setSenderName(entity.getUser().getName());
            dto.setNote(entity.getNote());

            System.out.println("packageName: " + dto.getPackageName());
            System.out.println("senderName: " + dto.getSenderName());

            dtoList.add(dto);
        }

        return dtoList;
    }



}
