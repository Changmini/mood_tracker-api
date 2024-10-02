package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.ChatService;
import kr.co.moodtracker.vo.ChatMessage;
import kr.co.moodtracker.vo.ChatRoom;
import kr.co.moodtracker.vo.UserVO;

//@RestController
//@EnableWebSocketMessageBroker
//public class ChatController extends CommonController {
//	
//	@Autowired
//	private ChatService chatService;
//	@Autowired
//	private Map<String, WebSocketSession> sessions = new HashMap<>();
//	
//	// 채팅방 생성
//    @PostMapping("/api/chat/rooms")
//    public String createRoom(HttpSession sess) 
//    		throws SessionNotFoundException {
//    	UserVO user = setUserInfo(sess);
//    	String roomId = "afefdsf46"; // randomInt
//    	String roomName = "1-2"; // hostUserId + "-" + guestUserId
//    	ChatRoom room = chatService.getChatRoom(roomName);
//    	if (room == null) {
//    		chatService.createChatRoom(user.getUserId(), roomId, roomName);    		
//    		return "Room created: " + roomName;
//    	}
//    	return "Existing room " + roomName;
//    }
//
//    @MessageMapping("/createRoom")
//    public void createRoom(WebSocketSession sess, String roomId) {
//    	HttpSession httpSess = (HttpSession) sess.getAttributes().get("httpSession");
//        chatService.createChatRoom(roomId, creator);
//    }
//
//    @MessageMapping("/joinRoom")
//    public void joinRoom(WebSocketSession sess, String roomId) {
//        chatService.joinRoom(roomId, participant);
//        sessions.put(participant, sess);
//    }
//
//    @MessageMapping("/leaveRoom")
//    public void leaveRoom(WebSocketSession sess, String roomId) {
//        chatService.leaveRoom(roomId, participant);
//        sessions.remove(participant);
//    }
//
//    @MessageMapping("/send")
//    public void send(ChatMessage chatMessage) {
//        chatService.sendMessage(chatMessage.getRoom(), chatMessage.getSender(), chatMessage.getContent());
//
//        // 각 참가자에게 직접 메시지를 전송
//        for (String participant : chatService.getChatRoom(chatMessage.getRoom()).getParticipants()) {
//            WebSocketSession session = sessions.get(participant);
//            if (session != null && session.isOpen()) {
//                try {
//                    session.sendMessage(new TextMessage(chatMessage.getSender() + ": " + chatMessage.getContent()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//	
//}
