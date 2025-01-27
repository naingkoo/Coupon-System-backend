package com.coupon.reposistory;

import com.coupon.entity.RevenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableJpaRepositories
public interface RevenueRepository extends JpaRepository<RevenueEntity, Integer> {

    @Query("SELECT r, b.name FROM RevenueEntity r JOIN r.businessEntity b")
    List<Object[]> findAllRevenueWithBusinessName();

    @Query("SELECT r, b.name FROM RevenueEntity r " +
            "JOIN r.businessEntity b " +
            "WHERE b.id = :businessId")
    List<Object[]> findRevenuesByBusinessId(@Param("businessId") Integer businessId);
}
