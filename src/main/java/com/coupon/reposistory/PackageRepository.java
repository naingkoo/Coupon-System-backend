package com.coupon.reposistory;

import com.coupon.entity.PackageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PackageRepository extends JpaRepository<PackageEntity,Integer>{

    // Custom JPQL query to fetch packages by businessId
    @Query("SELECT p FROM PackageEntity p WHERE p.business.id = :business_id")
    List<PackageEntity> findByBusinessId(Integer business_id);

    @Transactional
    @Modifying
    @Query("UPDATE PackageEntity p SET p.isDelete = true WHERE p.id = :id")
    void softDeleted(@Param("id")Integer id);

    @Query("SELECT p FROM PackageEntity p WHERE p.isDelete = false")
    List<PackageEntity> findAllActive();
}
