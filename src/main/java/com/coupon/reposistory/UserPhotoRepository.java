package com.coupon.reposistory;

import com.coupon.entity.UserPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhotoEntity, Integer> {

    Optional<UserPhotoEntity> findByUserId(Integer userId);

    @Query("SELECT up FROM UserPhotoEntity up WHERE up.user.id = :userId ORDER BY up.id ASC")
    List<UserPhotoEntity> findPhotosByUserId(@Param("userId") Integer userId);
}
