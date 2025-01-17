package com.coupon.reposistory;

import com.coupon.entity.ConfirmStatus;
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

    @Query("SELECT c FROM CouponEntity c JOIN FETCH c.purchase p JOIN FETCH c.packageEntity pe")
    List<CouponEntity> findAllCouponsWithPurchaseAndUser();


    @Query("SELECT c FROM CouponEntity c JOIN c.purchase p JOIN p.user u WHERE u.id = :userId")
    List<CouponEntity> findbyUserId(@Param("userId") Integer userId);

    Integer countByConfirm(ConfirmStatus confirm);

    @Query("SELECT COUNT(c) FROM CouponEntity c WHERE c.packageEntity.business.id = :businessId")
    Integer countByConfirmAndBusinessId( @Param("businessId") Integer businessId);

}
