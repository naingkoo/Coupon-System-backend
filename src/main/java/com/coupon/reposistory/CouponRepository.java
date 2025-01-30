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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
    List<CouponEntity> findByPurchaseId(Integer purchaseId);

    @Query("SELECT c FROM CouponEntity c JOIN c.purchase p WHERE p.user.id = :userId AND c.transfer_status = true AND c.used_status = true")
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
    @Query("UPDATE CouponEntity c SET c.used_status = false WHERE c.id = :id and c.used_status = true")
    int useCoupon(@Param("id") Integer id);

    @Query("SELECT DATE(p.purchase_date) AS purchaseDate, COUNT(c) AS confirmedCount " +
            "FROM CouponEntity c " +
            "JOIN c.purchase p " +
            "WHERE c.confirm = 'CONFIRM' " +
            "AND (:startDate IS NULL OR p.purchase_date >= :startDate) " +
            "AND (:endDate IS NULL OR p.purchase_date <= :endDate) " +
            "GROUP BY DATE(p.purchase_date) " +
            "ORDER BY DATE(p.purchase_date)")
    List<Object[]> countConfirmedCouponsByPurchaseDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    // Modify the query to accept businessId as a parameter
    @Query("SELECT p.purchase_date, COUNT(c) " +
            "FROM CouponEntity c " +
            "JOIN c.purchase p " +
            "JOIN c.packageEntity pkg " +
            "WHERE pkg.business.id = :businessId " +
            "AND p.purchase_date BETWEEN :startDate AND :endDate " +
            "GROUP BY p.purchase_date " +
            "ORDER BY p.purchase_date ASC")
    List<Object[]> countCouponsByBusinessAndPurchaseDate(Integer businessId, Date startDate, Date endDate);

    @Query("SELECT c FROM CouponEntity c JOIN c.packageEntity p WHERE p.business.id = :businessId AND c.confirm = CONFIRM")
    List<CouponEntity> findCouponsBybusinessId(@Param("businessId") Integer businessId);


    @Query("SELECT c FROM CouponEntity c JOIN c.packageEntity p WHERE p.business.id = :businessId AND c.confirm = CONFIRM AND used_status=false")
    List<CouponEntity> findscanedCouponsBybusinessId(@Param("businessId") Integer businessId);


    @Query("SELECT COUNT(c) FROM CouponEntity c WHERE c.packageEntity.id = :packageId AND c.confirm = :status")
    Integer countConfirmedCoupons(@Param("packageId") Integer packageId, @Param("status") ConfirmStatus status);

    @Query("SELECT c FROM CouponEntity c " +
            "JOIN c.packageEntity p " +
            "JOIN p.business b " +
            "JOIN c.purchase pu " +
            "WHERE b.id = :businessId " +
            "AND pu.purchase_date BETWEEN :startDate AND :endDate " +
            "AND c.confirm = 'CONFIRM' " +
            "AND c.paid_status = true")
    List<CouponEntity> findCouponsByBusinessAndDateRange(
            @Param("businessId") Integer businessId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    @Query("SELECT c FROM CouponEntity c WHERE "
            + "(LOWER(c.packageEntity.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR :searchText IS NULL OR :searchText = '') "
            + "AND (:startDate <= :endDate) "
            + "AND ("
            + "    (:selectedCategory = 'purchaseDate' "
            + "     AND ((DATE(c.purchase.purchase_date) = DATE(:startDate)) "
            + "          OR (DATE(c.purchase.purchase_date) BETWEEN DATE(:startDate) AND DATE(:endDate)))) "
            + "    OR (:selectedCategory = 'expiredDate' "
            + "        AND DATE(c.expired_date) BETWEEN DATE(:startDate) AND DATE(:endDate)) "
            + "    OR :selectedCategory IS NULL OR :selectedCategory = ''"
            + ")")
    List<CouponEntity> filterCoupons(@Param("searchText") String searchText,
                                     @Param("selectedCategory") String selectedCategory,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);


    @Query("SELECT c FROM CouponEntity c WHERE c.packageEntity.business.id = :businessId "
            + "AND (LOWER(c.packageEntity.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR :searchText IS NULL OR :searchText = '') "
            + "AND (:startDate <= :endDate) "
            + "AND ("
            + "    (:selectedCategory = 'purchaseDate' "
            + "     AND ((DATE(c.purchase.purchase_date) = DATE(:startDate)) "
            + "          OR (DATE(c.purchase.purchase_date) BETWEEN DATE(:startDate) AND DATE(:endDate)))) "
            + "    OR (:selectedCategory = 'expiredDate' "
            + "        AND DATE(c.expired_date) BETWEEN DATE(:startDate) AND DATE(:endDate)) "
            + "    OR :selectedCategory IS NULL OR :selectedCategory = ''"
            + ")")
    List<CouponEntity> filterCouponsWithBusinessId(@Param("businessId") Integer businessId,
                                                   @Param("searchText") String searchText,
                                                   @Param("selectedCategory") String selectedCategory,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);

    @Query("SELECT COUNT(c) FROM CouponEntity c WHERE c.confirm = :status")
    long countByConfirmStatus(@Param("status") ConfirmStatus status);

    @Query("SELECT c FROM CouponEntity c WHERE c.packageEntity.business.id = :businessId "
            + "AND (LOWER(c.packageEntity.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR :searchText IS NULL OR :searchText = '') "
            + "AND (:startDate <= :endDate) "
            + "AND ("
            + "    (:selectedCategory = 'purchaseDate' "
            + "     AND ((DATE(c.purchase.purchase_date) = DATE(:startDate)) "
            + "          OR (DATE(c.purchase.purchase_date) BETWEEN DATE(:startDate) AND DATE(:endDate)))) "
            + "    OR (:selectedCategory = 'expiredDate' "
            + "        AND DATE(c.expired_date) BETWEEN DATE(:startDate) AND DATE(:endDate)) "
            + "    OR :selectedCategory IS NULL OR :selectedCategory = ''"
            + ")"
            + "AND c.used_status = false")
    List<CouponEntity> findScanedCoupon(@Param("businessId") Integer businessId,
                                                   @Param("searchText") String searchText,
                                                   @Param("selectedCategory") String selectedCategory,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);

    @Query("SELECT c FROM CouponEntity c " +
            "JOIN c.purchase p " +
            "WHERE p.user.id = :userId AND c.used_status = false AND c.transfer_status = true")
    List<CouponEntity> findUsedCouponsByUserId(Integer userId);

}
