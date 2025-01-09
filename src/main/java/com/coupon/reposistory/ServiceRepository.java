package com.coupon.reposistory;

import com.coupon.entity.ServiceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity,Integer> {

    Optional<ServiceEntity> findById(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE ServiceEntity s SET s.isDelete = true WHERE s.id = :id")
    void softDeleted(@Param("id")Integer id);

    @Query("SELECT s FROM ServiceEntity s WHERE s.isDelete = false")
    List<ServiceEntity> findAllActiveServices();
}
