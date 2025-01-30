package com.coupon.service;

import com.coupon.entity.NotificationEntity;
import com.coupon.entity.NotificationStatus;
import com.coupon.entity.UserEntity;
import com.coupon.model.NotificationDTO;
import com.coupon.reposistory.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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

     public NotificationDTO sendTextNotification(String title,String context, UserEntity reciver_user){
         NotificationEntity notification=new NotificationEntity();
         notification.setUser(reciver_user);
         notification.setNotificationStatus(NotificationStatus.UNREAD);
         notification.setTitle(title);
         notification.setContent(Map.of(
                 "type","TEXT",
                 "context",context
         ));
         NotificationDTO notificationDTO=modelMapper.map(notificationRepository.save(notification), NotificationDTO.class);
         Map<String,Object> payload=new HashMap<>();
         payload.put("title","notification");
         payload.put("object",notificationDTO);
         messagingTemplate.convertAndSend(
                 "/queue/" + reciver_user.getId().toString(),payload
         );
       return notificationDTO;
     }

    public void sendTaskNotification(String title,String context,String action,UserEntity reciver_user,Object object) throws JsonProcessingException {
        NotificationEntity notification=new NotificationEntity();
        notification.setUser(reciver_user);
        notification.setNotificationStatus(NotificationStatus.UNREAD);
        notification.setTitle(title);
        String jsonObject=objectMapper.writeValueAsString(object);
        notification.setContent(Map.of(
                "type","TASK",
                "context",context,
                "action",action,
                "object",jsonObject
        ));
        notificationRepository.save(notification);
        sendByWebsocket("notification",object,reciver_user.getId());
    }

    public void sendByWebsocket(String title,Object object,Integer reciver_user){
        Map<String,Object> payload=new HashMap<>();
        payload.put("title",title);
        payload.put("object",object);
        messagingTemplate.convertAndSend(
                "/queue/" + reciver_user,payload
        );
    }
}
