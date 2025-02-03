package com.coupon.reposistory;

import com.coupon.entity.BusinessPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessPlanRepository extends JpaRepository<BusinessPlanEntity, Integer> {

    Optional<BusinessPlanEntity> findByBusinessId(Integer businessId);
}
