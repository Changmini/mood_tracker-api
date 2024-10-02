package kr.co.moodtracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//	
//	@Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat").withSockJS();
//    }
//	
//	@Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//		// 클라이언트가 구독할 수 있는 주제(prefix)를 설정
//        config.enableSimpleBroker("/topic");
//        // 서버에서 클라이언트로 메시지를 보낼 때 사용하는 접두사 설정
//        config.setApplicationDestinationPrefixes("/app");
//    }
//	
//}
