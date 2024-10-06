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
import kr.co.moodtracker.vo.ChatInfo;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
	
	@Autowired
	NeighborMapper neighborMapper;
	
	private final HashMap<String, Set<WebSocketSession>> CHAT_ROOMS = new HashMap<>();
	private final HashMap<WebSocketSession, ChatInfo> CHAT_INFO = new HashMap<>();
	private final JSONParser parser = new JSONParser();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 클라이언트 연결 시
		Long neighborId = Long.valueOf(session.getUri().getQuery().split("&")[0].split("=")[1]);
		String sender = (String) session.getUri().getQuery().split("&")[1].split("=")[1];
		
		String roomName = getRoomName(session, neighborId);
		if (createChatRoom(roomName)) {// 채팅방 생성
			neighborMapper.notifyNeighborsChatroomActive(neighborId);// 상대방에게 채팅방 개설을 알리기
		}
		joinUser(neighborId, sender, roomName, session); // 채팅방에 사용자 등록
		
		JSONObject msg = new JSONObject();
		msg.put("content", "채팅방에 입장하셨습니다.");
		session.sendMessage(new TextMessage(msg.toJSONString()));// 본인한테 메시지 보내기
	}

	@Override
	public void handleMessage(WebSocketSession self, WebSocketMessage<?> message) throws Exception {
		String payload = (String) message.getPayload();
		JSONObject param = (JSONObject) parser.parse(payload);
		
		Long neighborId = (Long) param.get("neighborId");
		String time = param.get("time").toString();
		String sender = param.get("sender").toString();
		String content = param.get("content").toString();
		
		String roomName = getRoomName(self, neighborId);
		chatting(CHAT_ROOMS.get(roomName), self, time, sender, content); // 채팅방에 메시지 보내기
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 에러 처리
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// 연결 종료 시 로직
		leaveUser(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	
	/**
	 * <pre>
	 *  새로운 채팅방 또는 등록된 채팅방의 이름을 리턴한다.
	 * </pre>
	 * @param session
	 * @param neighborId
	 * @return
	 * @throws Exception
	 */
	private String getRoomName(
				WebSocketSession session
				, Long neighborId
			) throws Exception {
		ChatInfo info = CHAT_INFO.get(session);// 등록된 채팅방의 이름
		if (info == null || info.getRoomName() == null) {
			return makeRoomId(neighborId);// 새로운 채팅방의 이름
		}
		return info.getRoomName();
	}
	
	/**
	 * <pre>
	 *  만약 채팅방이 없으면 새로운 채팅방 생성
	 * </pre>
	 * @param roomName
	 */
	private boolean createChatRoom(String roomName) {
		if (CHAT_ROOMS.containsKey(roomName))
			return false;
		CHAT_ROOMS.put(roomName, new HashSet<WebSocketSession>());
		return true;
	};

	/**
	 * <pre>
	 *  아직 채팅방에 속하지 않은 사용자라면,
	 *  본인이 속하는 채팅방을 찾아 채팅방에 귀속시킨다.
	 * </pre>
	 * @param roomName
	 * @param user
	 * @throws Exception
	 */
	private boolean joinUser(
				Long neighborId
				, String sender
				, String roomName
				, WebSocketSession user
			) throws Exception {
		if (roomName == null || roomName.trim().equals("") || user == null)
			throw new Exception("알 수 없는 채팅방 또는 사용자입니다: joinUser() Parameter Error");
		Set<WebSocketSession> chatRoom = CHAT_ROOMS.get(roomName);
		if (chatRoom == null)
			throw new Exception("존재하지 않는 방입니다.");
		if (chatRoom.contains(user))
			return false;
		chatRoom.add(user);
		CHAT_INFO.put(user, new ChatInfo.Builder()
				.neighborId(neighborId)
				.sender(sender)
				.roomName(roomName)
				.build());
		return true;
	};
	
	/**
	 * <pre>
	 *  세션 정보를 지울 때 사용한다.
	 *  해당 메서드를 사용하지 않고 두 객체를 각각 접근하여 세션을 지울 경우,
	 *  한 객체라도 데이터 삭제에 실패하면 메모리 누수가 발생할 수 있다.
	 * </pre>
	 * @param roomName
	 * @param session
	 * @throws Exception 
	 */
	private void leaveUser(WebSocketSession user) throws Exception {
		ChatInfo info = CHAT_INFO.get(user);
		if (info == null) 
			throw new Exception("등록되지 않은 사용자입니다: leaveUser");
		String roomName = info.getRoomName(); 
		if (roomName == null || roomName.trim().equals("") || user == null) {
			throw new Exception("알 수 없는 채팅방 또는 사용자입니다: leaveUser() Parameter Error");
		}
		Set<WebSocketSession> chatRoom = CHAT_ROOMS.get(roomName);
		chatRoom.remove(user);// 채팅방 나가기
		CHAT_INFO.remove(user);// 채팅방 정보 삭제
		neighborMapper.leaveChatroom(info.getNeighborId());// DB chatroom_active='N'
		if (chatRoom.isEmpty()) {// user.len == 0
			CHAT_ROOMS.remove(roomName);// 채팅방 삭제
		} else {// user.len > 0
			chatting(chatRoom
					, null
					, "채팅 종료"
					, info.getSender()
					, info.getSender()+"님이 채팅방을 나갔습니다."
			); // 채팅방에 메시지 보내기
		}
	}
	
	/**
	 * <pre>
	 *  채팅방의 본인을 제외한 모든 인원에게 메시지를 전달한다.
	 * <pre>
	 * @param ChatRoom
	 * @param self
	 * @param time
	 * @param sender
	 * @param content
	 */
	public void chatting(
				Set<WebSocketSession> ChatRoom
				, WebSocketSession self
				, String time
				, String sender
				, String content
			) {
		JSONObject msg = new JSONObject();
		msg.put("time", time);
		msg.put("sender", sender);
		msg.put("content", content);
		ChatRoom.forEach(user -> {
			if (!user.isOpen() || user.equals(self))
				return ; // skip
			try {
				user.sendMessage(new TextMessage(msg.toJSONString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * <pre>
	 *  neighborId를 통해 Database에서 profile_id 값을 구하고,
	 *  이를 바탕으로 roomName을 새로 생성한다. 
	 * </pre>
	 * @param neighborId
	 * @return
	 * @throws Exception
	 */
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
		throw new Exception("잘못된 데이터입니다. 관리자에게 문의하십시오: makeRoomId()");
	}// makeRoomId method
}
