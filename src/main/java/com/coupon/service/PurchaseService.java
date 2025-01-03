package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.PackageDTO;
import com.coupon.model.PurchaseDTO;
import com.coupon.reposistory.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void savePurchaseAndCoupons(PurchaseDTO purchaseDTO, List<PackageDTO> selectedPackages) {
        // Create and save PurchaseEntity
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setTotal_amount(purchaseDTO.getTotal_amount());
        purchaseEntity.setTotal_quantity(purchaseDTO.getTotal_quantity());
        purchaseEntity.setPayment_type(purchaseDTO.getPayment_type());
        purchaseEntity.setTransaction_id(purchaseDTO.getTransaction_id());
        purchaseEntity.setPurchase_date(new Date());

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
}