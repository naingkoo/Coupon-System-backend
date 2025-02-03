package com.coupon.service;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.BusinessPlanEntity;
import com.coupon.entity.PlanEntity;
import com.coupon.model.BusinessPlanDTO;
import com.coupon.reposistory.BusinessPlanRepository;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class BusinessPlanService {
    @Autowired
    private BusinessPlanRepository businessPlanRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private PlanRepository planRepository;

    public BusinessPlanDTO saveBusinessPlan(BusinessPlanDTO dto) {

        // Fetch BusinessEntity and PlanEntity by ID
        BusinessEntity business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
        PlanEntity plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Check if a BusinessPlan with the same businessId already exists
        BusinessPlanEntity existingBusinessPlan = businessPlanRepository.findByBusinessId(dto.getBusinessId())
                .orElse(null); // This will return null if no plan is found.

        BusinessPlanEntity businessPlan;

        if (existingBusinessPlan != null) {
            // If it exists, update the existing plan
            businessPlan = existingBusinessPlan;
            businessPlan.setTotal_amount(dto.getTotal_amount());
            businessPlan.setPaymentType(dto.getPaymentType());
            businessPlan.setTransaction_id(dto.getTransaction_id());
            businessPlan.setPayment_date(dto.getPayment_date());
            businessPlan.setPlan(plan); // Update the plan as well
        } else {
            // If it doesn't exist, create a new plan
            businessPlan = new BusinessPlanEntity();
            businessPlan.setTotal_amount(dto.getTotal_amount());
            businessPlan.setPaymentType(dto.getPaymentType());
            businessPlan.setTransaction_id(dto.getTransaction_id());
            businessPlan.setPayment_date(dto.getPayment_date());

            businessPlan.setBusiness(business);
            businessPlan.setPlan(plan); // Set the plan for the new plan
        }

        // Save or update the business plan
        BusinessPlanEntity saved = businessPlanRepository.save(businessPlan);

        // Convert the saved entity to DTO
        BusinessPlanDTO savedDto = new BusinessPlanDTO();
        savedDto.setId(saved.getId());
        savedDto.setTotal_amount(saved.getTotal_amount());
        savedDto.setPayment_date(saved.getPayment_date());
        savedDto.setBusinessId(saved.getBusiness().getId());
        savedDto.setPlanId(saved.getPlan().getId()); // Set the planId in DTO

        return savedDto;
    }


    public BusinessPlanDTO getBusinessPlanByBusinessId(Integer businessId) {
        Optional<BusinessPlanEntity> entityOptional = businessPlanRepository.findByBusinessId(businessId);
        return entityOptional.map(this::convertToDTO).orElse(null);
    }

    private BusinessPlanDTO convertToDTO(BusinessPlanEntity entity) {
        BusinessPlanDTO dto = new BusinessPlanDTO();
        dto.setId(entity.getId());
        dto.setTotal_amount(entity.getTotal_amount());
        dto.setPaymentType(entity.getPaymentType());
        dto.setTransaction_id(entity.getTransaction_id());
        dto.setPayment_date(entity.getPayment_date());
        dto.setBusinessId(entity.getBusiness().getId());
        dto.setPlanId(entity.getPlan().getId());

        // Fetch and set max_packages from PlanEntity
        dto.setMax_package(entity.getPlan().getMax_packages());

        return dto;
    }

}
