package com.coupon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public SocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/draw.ConnectionTest")
    public void userConnectedEvent(SimpMessageHeaderAccessor accessor, @Payload String load) {


        String simpSessionId = accessor.getSessionId();
        System.out.println("Received userConnectedEvent. Session ID: " + simpSessionId);

        String message = "Your session ID is: " + simpSessionId;


        SimpMessageHeaderAccessor responseAccessor = StompHeaderAccessor.create(SimpMessageType.MESSAGE);
        responseAccessor.setSessionId(simpSessionId);


        System.out.println("Sending message: " + message);
        messagingTemplate.convertAndSendToUser("john", "/topic/ConnectionTest", message, responseAccessor.getMessageHeaders());

    }

}
