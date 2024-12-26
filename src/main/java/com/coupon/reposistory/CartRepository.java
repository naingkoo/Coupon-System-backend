package com.coupon.reposistory;

import com.coupon.entity.CartEntity;
import com.coupon.entity.PackageEntity;
import com.coupon.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    CartEntity findByUserAndPackageEntity(UserEntity user, PackageEntity packageEntity);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.packageEntity p WHERE c.user.id = :userId")
    List<CartEntity> findByUserIdWithPackageData(Integer userId);
}
