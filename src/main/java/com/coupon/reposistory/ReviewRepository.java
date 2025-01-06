package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {


    List<ReviewEntity> findByid(Integer id);

//   List<ReviewEntity> findByBusinessEntity(BusinessEntity businessEntity);

    // Method to find reviews by businessId
    List<ReviewEntity> findByBusinessEntityId(Integer businessId);
}