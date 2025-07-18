package com.example.auction_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Defines message prefixes for topics the client can subscribe to.
        // Messages sent to destinations with "/topic" will be broadcast to all connected clients.
        registry.enableSimpleBroker("/topic");
        // Defines the prefix for messages that are bound for @MessageMapping-annotated methods.
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the "/ws" endpoint for WebSocket connections.
        // SockJS is used as a fallback for browsers that don't support WebSocket.
        registry.addEndpoint("/ws").withSockJS();
    }
}