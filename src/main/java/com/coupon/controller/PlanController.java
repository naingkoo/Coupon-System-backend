package com.coupon.controller;


import com.coupon.model.PlanDTO;
import com.coupon.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@CrossOrigin(origins = "http://localhost:4200")
public class PlanController {

    @Autowired
    private PlanService planService;

    // Create a new plan
    @PostMapping("/save")
    public ResponseEntity<PlanDTO> createPlan(@RequestBody PlanDTO plan) {
        PlanDTO savedPlan = planService.savePlan(plan);
        return ResponseEntity.ok(savedPlan);
    }

    // Get all plans
    @GetMapping("/getAll")
    public ResponseEntity<List<PlanDTO>> getAllPlans() {
        List<PlanDTO> plans = planService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    // Get a specific plan by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<PlanDTO> getPlanById(@PathVariable Integer id) {
        PlanDTO plan = planService.getPlanById(id);
        return ResponseEntity.ok(plan);
    }

    // Delete a plan by ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
//        planService.deletePlan(id);
//        return ResponseEntity.noContent().build();
//    }
}
