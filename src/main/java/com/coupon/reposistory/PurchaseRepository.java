package com.coupon.reposistory;

import com.coupon.entity.ConfirmStatus;
import com.coupon.entity.PurchaseEntity;
import com.coupon.model.PurchaseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {

    List<PurchaseEntity> findByUserId(Integer user_id);

    long countByConfirm(ConfirmStatus confirm);
}
