package com.coupon.reposistory;

import com.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
    List<CouponEntity> findByPurchaseId(Integer purchaseId);

    @Query("SELECT c FROM CouponEntity c JOIN c.purchase p WHERE p.user.id = :userId AND c.transfer_status = true")
    List<CouponEntity> findCouponsByUserId(@Param("userId") Integer userId);
}
