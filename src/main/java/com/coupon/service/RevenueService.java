package com.coupon.service;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.CouponEntity;
import com.coupon.entity.PaidCouponEntity;
import com.coupon.entity.RevenueEntity;
import com.coupon.model.PaidCouponDTO;
import com.coupon.model.RevenueDTO;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.CouponRepository;
import com.coupon.reposistory.PaidCouponRepository;
import com.coupon.reposistory.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private PaidCouponRepository paidCouponRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CouponRepository couponRepository;

    public RevenueService(RevenueRepository revenueRepository,
                          PaidCouponRepository paidCouponRepository,
                          BusinessRepository businessRepository,
                          CouponRepository couponRepository) {
        this.revenueRepository = revenueRepository;
        this.paidCouponRepository = paidCouponRepository;
        this.businessRepository = businessRepository;
        this.couponRepository = couponRepository;
    }

    public void saveRevenue(RevenueDTO revenueDTO) {
        // Retrieve BusinessEntity
        BusinessEntity businessEntity = businessRepository.findById(revenueDTO.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // Create RevenueEntity
        RevenueEntity revenueEntity = new RevenueEntity();
        revenueEntity.setPay_date(revenueDTO.getPayDate());
        revenueEntity.setFrom_date(revenueDTO.getFromDate());
        revenueEntity.setTo_date(revenueDTO.getToDate());
        revenueEntity.setTotal_amount(revenueDTO.getTotalAmount());
        revenueEntity.setPay_amount(revenueDTO.getPayAmount());
        revenueEntity.setPercentage(revenueDTO.getPercentage());
        revenueEntity.setQuantity(revenueDTO.getQuantity());
        revenueEntity.setBusinessEntity(businessEntity);

        // Save RevenueEntity
        RevenueEntity savedRevenue = revenueRepository.save(revenueEntity);

        // Create and save PaidCouponEntities
        List<PaidCouponDTO> paidCoupons = revenueDTO.getPaidCoupons();
        for (PaidCouponDTO paidCouponDTO : paidCoupons) {
            // Retrieve the CouponEntity using couponId
            CouponEntity couponEntity = couponRepository.findById(paidCouponDTO.getCouponId())
                    .orElseThrow(() -> new RuntimeException("Coupon not found with ID: " + paidCouponDTO.getCouponId()));

            // Update the status of the coupon to true (indicating it's paid)
            couponEntity.setPaid_status(true); // Assuming the status field is a boolean, adjust accordingly
            couponRepository.save(couponEntity); // Save the updated coupon entity

            // Create PaidCouponEntity
            PaidCouponEntity paidCouponEntity = new PaidCouponEntity();
            paidCouponEntity.setCoupon(couponEntity);
            paidCouponEntity.setRevenueEntity(savedRevenue);

            // Save PaidCouponEntity
            paidCouponRepository.save(paidCouponEntity);
        }
    }

    public List<RevenueDTO> getAllRevenueWithBusinessName() {
        List<Object[]> results = revenueRepository.findAllRevenueWithBusinessName();

        return results.stream().map(record -> {
            RevenueEntity revenue = (RevenueEntity) record[0];
            String businessName = (String) record[1];

            RevenueDTO dto = new RevenueDTO();
            dto.setId(revenue.getId());
            dto.setPayDate(revenue.getPay_date());
            dto.setFromDate(revenue.getFrom_date());
            dto.setToDate(revenue.getTo_date());
            dto.setTotalAmount(revenue.getTotal_amount());
            dto.setPayAmount(revenue.getPay_amount());
            dto.setPercentage(revenue.getPercentage());
            dto.setQuantity(revenue.getQuantity());
            dto.setBusinessId(revenue.getBusinessEntity().getId());
            dto.setBusinessName(businessName);

            return dto;
        }).collect(Collectors.toList());
    }

    public List<RevenueDTO> getRevenuesByBusinessId(Integer businessId) {
        List<Object[]> results = revenueRepository.findRevenuesByBusinessId(businessId);

        return results.stream().map(record -> {
            RevenueEntity revenue = (RevenueEntity) record[0];
            String businessName = (String) record[1];

            RevenueDTO dto = new RevenueDTO();
            dto.setId(revenue.getId());
            dto.setPayDate(revenue.getPay_date());
            dto.setFromDate(revenue.getFrom_date());
            dto.setToDate(revenue.getTo_date());
            dto.setTotalAmount(revenue.getTotal_amount());
            dto.setPayAmount(revenue.getPay_amount());
            dto.setPercentage(revenue.getPercentage());
            dto.setQuantity(revenue.getQuantity());
            dto.setBusinessId(revenue.getBusinessEntity().getId());
            dto.setBusinessName(businessName);

            return dto;
        }).collect(Collectors.toList());
    }

}
