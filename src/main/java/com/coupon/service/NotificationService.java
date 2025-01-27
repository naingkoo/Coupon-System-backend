package com.coupon.service;

import com.coupon.reposistory.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
@Autowired
NotificationRepository notificationRepository;

    public boolean softDeleteNotification(Integer id) {
        int rowsAffected = notificationRepository.softDeleteNoti(id);
        return rowsAffected > 0;
    }

    public int countUnreadNotifications(Integer userid){
        return notificationRepository.countUnreadNotifications(userid);
    }
     public Boolean makeRead(Integer notiId){
         int rowsAffected = notificationRepository.makeRead(notiId);
         return rowsAffected > 0;
     }
}
