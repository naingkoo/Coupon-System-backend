package com.coupon.reposistory;

import com.coupon.entity.BusinessCategoryEntity;
import com.coupon.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategoryEntity,Integer> {

    List<BusinessCategoryEntity> findByBusiness(BusinessEntity business);

    void deleteByBusinessId(Integer id);

    // Custom query to find category IDs by business ID
    @Query("SELECT bc.category.id FROM BusinessCategoryEntity bc WHERE bc.business.id = :businessId")
    List<Integer> findCategoryIdsByBusinessId(@Param("businessId") Integer businessId);


}
