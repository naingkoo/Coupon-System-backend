package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.BusinessServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessServiceRepository extends JpaRepository<BusinessServiceEntity,Integer> {

    List<BusinessServiceEntity> findByBusiness(BusinessEntity business);

    void deleteByBusinessId(Integer id);

    // Custom query to find category IDs by business ID
    @Query("SELECT bc.service.id FROM BusinessServiceEntity bc WHERE bc.business.id = :id")
    List<Integer> findServiceIdsByBusinessId(@Param("id") Integer id);
}
