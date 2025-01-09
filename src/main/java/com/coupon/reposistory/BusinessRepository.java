package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity,Integer> {

    Optional<BusinessEntity> findById(Integer id);

    @Query("UPDATE BusinessEntity b SET b.isDelete = false WHERE b.id = :id")
    void deleteBusinessById(@Param("id") Integer id);

    @Query("SELECT b FROM BusinessEntity b WHERE b.isDelete = false")
    List<BusinessEntity> findAllActiveBusinesses();

    @Query("SELECT b FROM BusinessEntity b WHERE b.user.id = :userId AND b.isDelete = false")
    List<BusinessEntity> findAllActiveBusinessesByUserId(@Param("userId") Integer userId);

    long countByIsDeleteFalse();
}
