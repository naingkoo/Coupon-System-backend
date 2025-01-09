package com.coupon.reposistory;

import com.coupon.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer> {

    Optional<CategoryEntity> findById(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE CategoryEntity c SET c.isDelete = true WHERE c.id = :id")
    void softDeleted(@Param("id")Integer id);

    @Query("SELECT c FROM CategoryEntity c WHERE c.isDelete = false")
    List<CategoryEntity> findAllActiveCategories();
}
