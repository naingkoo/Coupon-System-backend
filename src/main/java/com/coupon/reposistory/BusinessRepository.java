package com.coupon.reposistory;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity,Integer> {

    Optional<BusinessEntity> findById(Integer id);
}
