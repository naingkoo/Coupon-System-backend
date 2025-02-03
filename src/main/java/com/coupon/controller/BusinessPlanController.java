package com.coupon.controller;


import com.coupon.entity.BusinessPlanEntity;
import com.coupon.model.BusinessPlanDTO;
import com.coupon.service.BusinessPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business-plan")
public class BusinessPlanController {

    @Autowired
    private BusinessPlanService businessPlanService;

    @PostMapping("/save")
    public ResponseEntity<BusinessPlanDTO> saveBusinessPlan(@RequestBody BusinessPlanDTO businessPlanDTO) {
        BusinessPlanDTO savedEntity = businessPlanService.saveBusinessPlan(businessPlanDTO);
        return ResponseEntity.ok(savedEntity);
    }

    @GetMapping("/public/by-business/{businessId}")
    public BusinessPlanDTO getBusinessPlanByBusinessId(@PathVariable Integer businessId) {
        return businessPlanService.getBusinessPlanByBusinessId(businessId);
    }
}
