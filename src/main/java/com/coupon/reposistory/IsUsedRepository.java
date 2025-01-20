package com.coupon.reposistory;

import com.coupon.entity.IsUsedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IsUsedRepository extends JpaRepository<IsUsedEntity,Integer> {
}
