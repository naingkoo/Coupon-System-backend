package com.coupon.model;

import com.coupon.entity.NotificationStatus;
import com.coupon.entity.UserEntity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class NotificationDTO {
    private Integer id;
    private Date noti_date;
    private Integer userId;
    private NotificationStatus notificationStatus;
    private String title;
    private Map<String, Object> content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getNoti_date() {
        return noti_date;
    }

    public void setNoti_date(Date noti_date) {
        this.noti_date = noti_date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
