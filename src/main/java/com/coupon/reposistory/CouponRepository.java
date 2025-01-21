package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.ConfirmStatus;
import com.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
    List<CouponEntity> findByPurchaseId(Integer purchaseId);

    @Query("SELECT c FROM CouponEntity c JOIN c.purchase p WHERE p.user.id = :userId AND c.transfer_status = true")
    List<CouponEntity> findCouponsByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM CouponEntity c JOIN FETCH c.purchase p JOIN FETCH c.packageEntity pe")
    List<CouponEntity> findAllCouponsWithPurchaseAndUser();


    @Query("SELECT c FROM CouponEntity c JOIN c.purchase p JOIN p.user u WHERE u.id = :userId")
    List<CouponEntity> findbyUserId(@Param("userId") Integer userId);

    Integer countByConfirm(ConfirmStatus confirm);

    @Query("SELECT COUNT(c) FROM CouponEntity c WHERE c.packageEntity.business.id = :businessId AND c.confirm = :confirm")
    Long countByConfirmAndBusinessId(@Param("confirm") ConfirmStatus confirm, @Param("businessId") Long businessId);


    @Query("SELECT p.business FROM CouponEntity c " +
            "JOIN c.packageEntity p " +
            "WHERE c.id = :couponId")
    BusinessEntity findBusinessByCouponId(@Param("couponId") Integer couponId);
    @Query("SELECT c FROM CouponEntity c WHERE c.qr.code = :couponcode AND c.packageEntity.business.id = :bussinessID AND c.used_status=true")
    Optional<CouponEntity> searchCoupon(@Param("bussinessID") Integer bussinessID, @Param("couponcode") String couponcode);

    @Modifying
    @Transactional
    @Query("UPDATE CouponEntity c SET c.used_status = false WHERE c.id = :id")
    int useCoupon(@Param("id") Integer id);



}
