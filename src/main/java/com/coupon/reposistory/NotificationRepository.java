package com.coupon.reposistory;

import com.coupon.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {
    @Query("SELECT noti FROM NotificationEntity noti WHERE noti.user.id = :userId")
    List<NotificationEntity> findByUser(@Param("userId") Integer userId);

}
