package com.coupon.reposistory;

import com.coupon.entity.CartEntity;
import com.coupon.entity.PackageEntity;
import com.coupon.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    CartEntity findByUserAndPackageEntity(UserEntity user, PackageEntity packageEntity);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.packageEntity p WHERE c.user.id = :userId")
    List<CartEntity> findByUserIdWithPackageData(Integer userId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM CartEntity c WHERE c.user.id = :userId AND c.packageEntity.id = :packageId")
    boolean existsByUser_idAndPackage_id(@Param("userId") Integer userId,
                                         @Param("packageId") Integer packageId);

    // Delete cart data by user ID and package ID
    @Modifying
    @Query("DELETE FROM CartEntity c WHERE c.user.id = :userId AND c.packageEntity.id = :packageId")
    void deleteByUserIdAndPackageId(@Param("userId") Integer userId,
                                    @Param("packageId") Integer packageId);
    //void deleteByUserIdAndPackageId(Integer user_id, Integer package_id);
}
