package kr.co.moodtracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.moodtracker.vo.ChatRoom;

@Service
public class ChatService {
	private final Map<String, ChatRoom> chatRooms = new HashMap<>();
	
	public void createChatRoom(int userId, String roomId, String roomName) {
		chatRooms.put(roomId, new ChatRoom(userId, roomId, roomName));
	}
	
    public void joinRoom(int userId, String roomId) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.addParticipant(userId);
        }
    }

    public void leaveRoom(int userId, String roomId) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.removeParticipant(userId);
            if (room.isEmpty()) {
                chatRooms.remove(roomId); // 모든 사용자가 나가면 방 삭제
            }
        }
    }
	public ChatRoom getChatRoom(String roomId) {
		return chatRooms.get(roomId);
	}
	
	public Map<String, ChatRoom> getChatRooms() {
		return chatRooms;
	}
}
