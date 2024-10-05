package kr.co.moodtracker.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import kr.co.moodtracker.mapper.NeighborMapper;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
	
	@Autowired
	NeighborMapper neighborMapper;
	
	private final HashMap<String, Set<WebSocketSession>> chatRooms = new HashMap<>();
	private final HashMap<WebSocketSession, String> roomNameOfSession = new HashMap<>();
	private final JSONParser parser = new JSONParser();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 클라이언트 연결 시
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		/* roomId : username : message */
		String payload = (String) message.getPayload();
		JSONObject param = (JSONObject) parser.parse(payload);
		
		Long neighborId = (Long) param.get("neighborId");
		String time = param.get("time").toString();
		String sender = param.get("sender").toString();
		String content = param.get("content").toString();
		String roomId = roomNameOfSession.get(session);
		roomId = roomId==null ? makeRoomId(neighborId) : roomId; 
		
		Set<WebSocketSession> roomSessions = chatRooms.get(roomId);
		if (roomSessions == null) {
			roomSessions = new HashSet<>();
			roomSessions.add(session);
			chatRooms.put(roomId, roomSessions);
			roomNameOfSession.put(session, roomId);
		} else if (!roomSessions.contains(session)) {
			roomSessions.add(session);
			roomNameOfSession.put(session, roomId);
		}
		
		JSONObject res = new JSONObject();
		res.put("time", time);
		res.put("sender", sender);
		res.put("content", content);
		roomSessions.forEach(roomS -> {
			if (!roomS.isOpen() || roomS.equals(session))
				return ; // skip
			try {
				roomS.sendMessage(new TextMessage(res.toJSONString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 에러 처리
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// 연결 종료 시 로직
		String roomId = roomNameOfSession.get(session);
		if (roomId == null)
			return ;
		roomNameOfSession.remove(session);
		Set<WebSocketSession> chatSessions = chatRooms.get(roomId);
		chatSessions.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	private String makeRoomId(Long neighborId) throws Exception {
		if (neighborId == null || neighborId < 1)
			throw new Exception("이웃이 아닌 사람과 채팅을 시도할 수 없습니다.");
		List<Map<String,Integer>> profileIds = neighborMapper.getGroupProfileId(neighborId);
		for (Map<String, Integer> map : profileIds) {
			Integer x = (Integer) map.get("host_profile_id");
			Integer y = (Integer) map.get("guest_profile_id");
			if (x==null || y==null || x==0 || y==0)
				return null; // 서버 이상으로 채팅을 시도할 수 없습니다.
			return "couple-" + (x+y);
			
		}
		throw new Exception("잘못된 데이터입니다. 관리자에게 문의하십시오.");
	}// makeRoomId method

}
