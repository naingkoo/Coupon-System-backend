package com.coupon.reposistory;

import com.coupon.entity.QREntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRRepository extends JpaRepository<QREntity, Integer> {

    Optional<QREntity> findByCouponId(Integer couponId);
}
