package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.Map;
import com.coupon.converter.MapToJsonConverter;

@Entity
@Table(name="Notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "noti_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date noti_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name = "Title")
    private String title;

    @Column(name = "Content", columnDefinition = "TEXT")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> content;

    public Integer getId() {
        return id;
    }

    public NotificationEntity() {
    }

    public NotificationEntity(Date noti_date, UserEntity user, NotificationStatus notificationStatus, String title, Map<String, Object> content) {
        this.noti_date = noti_date;
        this.user = user;
        this.notificationStatus = notificationStatus;
        this.title = title;
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getNoti_date() {
        return noti_date;
    }

    public void setNoti_date() {
        this.noti_date =new Date();
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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