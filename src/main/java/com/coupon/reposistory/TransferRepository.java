package com.coupon.reposistory;

import com.coupon.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity,Integer> {

    List<TransferEntity> findByUserId(Integer sender_id);

    @Query("SELECT t FROM TransferEntity t WHERE t.receiver_id = :receiver_id")
    List<TransferEntity> findByReceiverId(@Param("receiver_id") Integer receiver_id);
}
