package com.coupon.service;

import com.coupon.entity.CouponEntity;
import com.coupon.model.CouponDTO;
import com.coupon.entity.QREntity;
import com.coupon.reposistory.CouponRepository;
import com.coupon.reposistory.QRRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private QRRepository qrRepository;

    @Transactional
    public void confirmPurchaseAndGenerateQR(Integer purchaseId) {
        // Retrieve all coupons by purchase ID
        List<CouponEntity> coupons = couponRepository.findByPurchaseId(purchaseId);

        if (coupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the given purchase ID.");
        }

        // Update confirm status for all coupons
        coupons.forEach(coupon -> coupon.setConfirm(false));
        couponRepository.saveAll(coupons);

        // Generate QR codes for each coupon and save to QR table
        List<QREntity> qrEntities = coupons.stream().map(coupon -> {
            QREntity qr = new QREntity();
            qr.setCode(generateRandomCode(50)); // Generate random 50-character code
            qr.setCoupon(coupon);
            return qr;
        }).collect(Collectors.toList());

        qrRepository.saveAll(qrEntities);
    }

    private String generateRandomCode(int length) {
        final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>?/~";
        StringBuilder code = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(index));
        }
        return code.toString();
    }

    public List<CouponDTO> showCouponbyUserId(Integer userId) {

        List<CouponEntity> coupon = couponRepository.findCouponsByUserId(userId);

        List<CouponDTO> dtoList = new ArrayList<>();
        for(CouponEntity entity: coupon) {

            if (entity.getTransfer_status() == true) {
                CouponDTO dto = new CouponDTO();
                dto.setId(entity.getId());
                dto.setExpired_date(entity.getExpired_date());
                dto.setConfirm(entity.getConfirm());
                if (entity.getPackageEntity() != null) {
                    dto.setPackageName(entity.getPackageEntity().getName());
                    dto.setImage(entity.getPackageEntity().getImage());
                }


                System.out.println("packageName: " + dto.getPackageName());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
}






