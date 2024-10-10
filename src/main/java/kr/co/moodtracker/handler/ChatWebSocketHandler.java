package kr.co.moodtracker.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import kr.co.moodtracker.enums.MessageType;
import kr.co.moodtracker.mapper.NeighborMapper;
import kr.co.moodtracker.vo.Chat;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
	
	@Autowired
	NeighborMapper neighborMapper;
	
	private final Chat CHAT = new Chat();
	private final JSONParser parser = new JSONParser();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 클라이언트 연결 시
		Long neighborId = Long.valueOf(session.getUri().getQuery().split("&")[0].split("=")[1]);
		String sender = (String) session.getUri().getQuery().split("&")[1].split("=")[1];
		
		String roomName = CHAT.getRoomName(session, neighborMapper, neighborId);
		if (CHAT.createChatRoom(roomName)) {// 채팅방 생성
			neighborMapper.notifyNeighborsChatroomActive(neighborId);// 상대방에게 채팅방 개설을 알리기
		}
		CHAT.joinUser(session, neighborId, sender, roomName); // 채팅방에 사용자 등록
		
		CHAT.chatting(session
				, MessageType.ME
				, roomName
				, null// 시간 표시, 필요 x
				, null// 본인에게 입장 메시지를 보내는 것이라, 필요 x
				, "채팅방에 입장하셨습니다."); // 채팅방에 메시지 보내기
	}

	@Override
	public void handleMessage(WebSocketSession self, WebSocketMessage<?> message) throws Exception {
		String payload = (String) message.getPayload();
		JSONObject param = (JSONObject) parser.parse(payload);
		
		Long neighborId = (Long) param.get("neighborId");
		String time = param.get("time").toString();
		String sender = param.get("sender").toString();
		String content = param.get("content").toString();
		
		String roomName = CHAT.getRoomName(self, neighborId);
		CHAT.chatting(self
				, MessageType.OTHER
				, roomName
				, time
				, sender
				, content); // 채팅방에 메시지 보내기
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 에러 처리
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// 연결 종료 시 로직
		CHAT.leaveUser(session, neighborMapper);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
