package com.coupon.reposistory;


import com.coupon.entity.PaidCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface PaidCouponRepository extends JpaRepository<PaidCouponEntity, Integer> {

    @Query("SELECT p FROM PaidCouponEntity p " +
            "JOIN FETCH p.coupon c " +
            "JOIN FETCH c.packageEntity pe " +
            "WHERE p.revenueEntity.id = :revenueId")
    List<PaidCouponEntity> findByRevenueId(Integer revenueId);
}
