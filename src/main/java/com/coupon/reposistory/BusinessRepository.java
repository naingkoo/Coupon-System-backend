package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.OpenStatus;
import com.coupon.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity,Integer> {

    Optional<BusinessEntity> findById(Integer id);

    Optional<BusinessEntity> findByUserId(Integer userId);

    @Modifying
    @Query("UPDATE BusinessEntity b SET b.isDelete = true WHERE b.id = :id")
    void deleteBusinessById(@Param("id") Integer id);

    @Query("SELECT b FROM BusinessEntity b WHERE b.isDelete = false")
    List<BusinessEntity> findAllActiveBusinesses();

    @Query("SELECT b FROM BusinessEntity b WHERE b.user.id = :userId AND b.isDelete = false")
    List<BusinessEntity> findAllActiveBusinessesByUserId(@Param("userId") Integer userId);

    long countByIsDeleteFalse();

    @Query("SELECT b FROM BusinessEntity b WHERE b.isDelete = true")
    List<BusinessEntity> findAllDeletedBusinesses();

    @Modifying
    @Transactional
    @Query("UPDATE BusinessEntity b SET b.isDelete = false WHERE b.id = :id")
    void restoreBusiness(@Param("id") Integer id);


    @Modifying
    @Transactional
    @Query("UPDATE BusinessEntity b SET b.isOpen = :status WHERE b.id = :id")
    void closeBusiness(@Param("id") Integer id, @Param("status")OpenStatus status);


    @Modifying
    @Transactional
    @Query("UPDATE BusinessEntity b SET b.isOpen = :status WHERE b.id = :id")
    void openBusiness(@Param("id") Integer id, @Param("status")OpenStatus status);

}
