package com.coupon.reposistory;

import com.coupon.entity.PaymentEntity;
import com.coupon.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Integer> {
    PaymentMethodEntity findByPaymentType (String paymentType);
}