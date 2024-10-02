package kr.co.moodtracker.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatRoom {
	private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
	private Set<Integer> participants = new HashSet<>();
	private List<ChatMessage> messages = new ArrayList<>();
	
	public ChatRoom(int userId, String roomId, String roomName) {
		this.participants.add(userId);
		this.roomId = roomId;
		this.roomName = roomName;
	}
	
	public Set<Integer> getParticipants(int userId) {
		return participants;
	}
	public void addParticipant(int userId) {
		participants.add(userId);
	}
	public void removeParticipant(int userId) {
        participants.remove(userId);
    }
	
	public List<ChatMessage> getMessages() {
		return messages;
	}
	public void addMessage(ChatMessage message) {
		messages.add(message);
	}

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}
