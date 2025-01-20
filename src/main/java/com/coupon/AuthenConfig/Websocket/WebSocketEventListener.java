package com.coupon.AuthenConfig.Websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class WebSocketEventListener extends TextWebSocketHandler{

    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        System.out.println("WebSocket connected: Session ID = " + sessionId);
        // Optionally associate the session ID with a user
        sessionUserMap.put(sessionId, "UnknownUser"); // Replace "UnknownUser" with actual logic
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessionUserMap.remove(sessionId);
        System.out.println("WebSocket disconnected: Session ID = " + sessionId);
    }

    public String getUsernameBySessionId(String sessionId) {
        return sessionUserMap.get(sessionId);
    }
}
