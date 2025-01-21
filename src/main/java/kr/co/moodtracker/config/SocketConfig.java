package kr.co.moodtracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kr.co.moodtracker.handler.ChatWebSocketHandler;

@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {
	
	@Autowired
	ChatWebSocketHandler chatWebSocketHandler;
	
	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat")
        	.setAllowedOrigins("*");
    }
	
}
