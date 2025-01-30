package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.PackageDTO;
import com.coupon.model.PurchaseDTO;
import com.coupon.reposistory.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private CartRepository cartRepository; // Inject CartRepository

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public PurchaseService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void savePurchaseAndCoupons(PurchaseDTO purchaseDTO, List<PackageDTO> selectedPackages) {
        // Create and save PurchaseEntity
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setTotal_amount(purchaseDTO.getTotal_amount());
        purchaseEntity.setTotal_quantity(purchaseDTO.getTotal_quantity());
        purchaseEntity.setPayment_type(purchaseDTO.getPayment_type());
        purchaseEntity.setTransaction_id(purchaseDTO.getTransaction_id());
        purchaseEntity.setPurchase_date(new Date());
        purchaseEntity.setConfirm(ConfirmStatus.PENDING);

        UserEntity user = new UserEntity(); // Assuming UserEntity is fetched by ID
        user.setId(purchaseDTO.getUser_id());
        purchaseEntity.setUser(user);

        purchaseEntity = purchaseRepository.save(purchaseEntity);

        // Process each selected package
        for (PackageDTO packageDTO : selectedPackages) {
            // Fetch the package entity
            PackageEntity packageEntity = packageRepository.findById(packageDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Package not found: " + packageDTO.getId()));

            // Check if sufficient quantity is available
            if (packageEntity.getQuantity() < packageDTO.getSelected_quantity()) {
                throw new RuntimeException("Insufficient quantity for package: " + packageDTO.getName());
            }

            // Reduce the package quantity
            packageEntity.setQuantity(packageEntity.getQuantity() - packageDTO.getSelected_quantity());
            packageRepository.save(packageEntity);

            // Create and save CouponEntity for each quantity
            for (int i = 0; i < packageDTO.getSelected_quantity(); i++) {
                CouponEntity couponEntity = new CouponEntity();
                couponEntity.setExpired_date(packageDTO.getExpired_date());
                couponEntity.setPurchase(purchaseEntity);
                couponEntity.setPackageEntity(packageEntity);

                couponRepository.save(couponEntity);
            }

            // Check if cart data exists for this user and package
            if (cartRepository.existsByUser_idAndPackage_id(purchaseDTO.getUser_id(), packageDTO.getId())) {
                // Delete cart data for this user and package
                cartRepository.deleteByUserIdAndPackageId(purchaseDTO.getUser_id(), packageDTO.getId());
            }
        }
        messagingTemplate.convertAndSend("/topic/purchases","One new payment request! Check this out.");
    }



    public List<PurchaseDTO> getAllPurchasesWithUserDetails() {
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<PurchaseEntity> purchases = purchaseRepository.findAll();

        for (PurchaseEntity purchase : purchases) {
            PurchaseDTO dto = new PurchaseDTO();
            dto.setId(purchase.getId());
            dto.setTotal_amount(purchase.getTotal_amount());
            dto.setTotal_quantity(purchase.getTotal_quantity());
            dto.setPayment_type(purchase.getPayment_type());
            dto.setTransaction_id(purchase.getTransaction_id());
            dto.setPurchase_date(purchase.getPurchase_date());
            dto.setConfirm(purchase.getConfirm().name());

            UserEntity user = purchase.getUser();
            dto.setUser_id(user.getId());
            dto.setUser_name(user.getName());

            // Get the first photo for the user
            Optional<UserPhotoEntity> userPhoto = userPhotoRepository.findPhotosByUserId(user.getId()).stream().findFirst();
            dto.setUser_photo(userPhoto.map(UserPhotoEntity::getName).orElse(null));

            purchaseDTOs.add(dto);
        }
        return purchaseDTOs;
    }

    public List<PurchaseDTO> getPurchasesByUserId(Integer user_id) {
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<PurchaseEntity> purchases = purchaseRepository.findByUserId(user_id);

        for (PurchaseEntity purchase : purchases) {
            PurchaseDTO dto = new PurchaseDTO();
            dto.setId(purchase.getId());
            dto.setTotal_amount(purchase.getTotal_amount());
            dto.setTotal_quantity(purchase.getTotal_quantity());
            dto.setPayment_type(purchase.getPayment_type());
            dto.setTransaction_id(purchase.getTransaction_id());
            dto.setPurchase_date(purchase.getPurchase_date());
            dto.setConfirm(purchase.getConfirm().name());

            purchaseDTOs.add(dto);
        }

        return purchaseDTOs;
    }

    public Map<String, Integer> getPaymentTypeDistribution() {
        return purchaseRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        PurchaseEntity::getPayment_type,
                        Collectors.summingInt(e -> 1) // Summing integers instead of counting longs
                ));
    }

    public long getConfirmedPurchasesCount() {
        return purchaseRepository.countByConfirm(ConfirmStatus.CONFIRM);
    }
}
