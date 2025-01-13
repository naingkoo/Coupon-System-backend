package com.coupon.AuthenConfig.Websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200") // Allow multiple origins for production, staging
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for app messages
        registry.enableSimpleBroker("/topic", "/queue");  // Enable /topic and /queue for subscription
        registry.setUserDestinationPrefix("/user");  // User-specific destinations, like /user/{userId}
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(256 * 1024);  // Increased message size limit (if needed)
        registry.setSendTimeLimit(30 * 1000);  // Increased time limit to 30 seconds
        registry.setSendBufferSizeLimit(1024 * 1024);  // Increased buffer size to 1 MB
    }
}
