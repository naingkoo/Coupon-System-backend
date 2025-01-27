package com.coupon.service;

import com.coupon.entity.PaidCouponEntity;
import com.coupon.model.PaidCouponDTO;
import com.coupon.reposistory.PaidCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaidCouponService {

    @Autowired
    private PaidCouponRepository paidCouponRepository;

    public List<PaidCouponDTO> getCouponsByRevenueId(Integer revenueId) {
        List<PaidCouponEntity> paidCoupons = paidCouponRepository.findByRevenueId(revenueId);

        return paidCoupons.stream().map(paidCoupon -> {
            PaidCouponDTO dto = new PaidCouponDTO();
            dto.setId(paidCoupon.getId());
            dto.setCouponId(paidCoupon.getCoupon().getId());
            dto.setRevenueId(paidCoupon.getRevenueEntity().getId());
            dto.setPackageName(paidCoupon.getCoupon().getPackageEntity().getName());
            dto.setPackageImage(paidCoupon.getCoupon().getPackageEntity().getImage());
            return dto;
        }).collect(Collectors.toList());
    }
}
