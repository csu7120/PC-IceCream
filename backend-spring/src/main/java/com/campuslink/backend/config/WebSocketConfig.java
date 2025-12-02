package com.campuslink.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   // <— STOMP 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // 안드로이드/웹이 접속할 WebSocket 엔드포인트
        registry.addEndpoint("/ws")         // ws://서버주소/ws
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 서버 → 클라이언트로 보내는 메시지(prefix)
        registry.enableSimpleBroker("/topic", "/queue");

        // 클라이언트 → 서버 메시지(prefix)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
