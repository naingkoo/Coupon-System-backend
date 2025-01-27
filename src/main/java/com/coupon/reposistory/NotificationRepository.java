package com.coupon.reposistory;

import com.coupon.entity.NotificationEntity;
import com.coupon.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {
    @Query("SELECT noti FROM NotificationEntity noti WHERE noti.user.id = :userId AND noti.notificationStatus != NotificationStatus.DELETED")
    List<NotificationEntity> findByUser(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.notificationStatus = 'DELETED' WHERE n.id = :id")
    int softDeleteNoti(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.notificationStatus = 'READ' WHERE n.id = :id")
    int makeRead(@Param("id") Integer id);


    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.notificationStatus = NotificationStatus.UNREAD and n.user.id=:userid")
    int countUnreadNotifications(@Param("userid") Integer userid);

}
