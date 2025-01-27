package com.coupon.service;

import com.coupon.entity.PlanEntity;
import com.coupon.model.PlanDTO;
import com.coupon.reposistory.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    // Save a new plan
    public PlanDTO savePlan(PlanDTO planDTO) {
        PlanEntity planEntity = convertToEntity(planDTO);
        PlanEntity savedPlan = planRepository.save(planEntity);
        return convertToDTO(savedPlan);
    }

    // Retrieve all plans
    public List<PlanDTO> getAllPlans() {
        List<PlanEntity> planEntities = planRepository.findAll();
        return planEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Retrieve a specific plan by ID
    public PlanDTO getPlanById(Integer id) {
        PlanEntity planEntity = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return convertToDTO(planEntity);
    }

    // Delete a plan by ID
    public void deletePlan(Integer id) {
        planRepository.deleteById(id);
    }

    // Convert DTO to Entity
    private PlanEntity convertToEntity(PlanDTO planDTO) {
        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(planDTO.getId());
        planEntity.setName(planDTO.getName());
        planEntity.setMax_packages(planDTO.getMax_packages());
        planEntity.setPrice(planDTO.getPrice());
        return planEntity;
    }

    // Convert Entity to DTO
    private PlanDTO convertToDTO(PlanEntity planEntity) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(planEntity.getId());
        planDTO.setName(planEntity.getName());
        planDTO.setMax_packages(planEntity.getMax_packages());
        planDTO.setPrice(planEntity.getPrice());
        return planDTO;
    }
}
