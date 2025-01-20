package com.coupon.reposistory;

import com.coupon.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {
}
