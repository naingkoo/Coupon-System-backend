package com.coupon.AuthenConfig.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200","https://cbmf9vrn-4200.asse.devtunnels.ms") // Allow multiple origins for production, staging
                .addInterceptors(customInterceptor())
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
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(argumentResolvers);
//    }

    @Bean
    public CustomInterceptor customInterceptor() {
        return new CustomInterceptor();
    }



}
