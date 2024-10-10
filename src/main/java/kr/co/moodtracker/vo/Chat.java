package kr.co.moodtracker.vo;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import kr.co.moodtracker.enums.MessageType;
import kr.co.moodtracker.mapper.NeighborMapper;

public class Chat {
	
	private final HashMap<String, Set<WebSocketSession>> ROOMS = new HashMap<>();
	private final HashMap<WebSocketSession, ChatInfo> CHAT_INFO = new HashMap<>();
	
	private class ChatInfo {
		Long neighborId;
		String name;
		String roomName;
		private ChatInfo(Long neighborId, String name, String roomName) {
			this.neighborId = neighborId;
			this.name = name;
			this.roomName = roomName;
		}
		public Long getNeighborId() {
			return neighborId;
		}
		public String getName() {
			return name;
		}
		public String getRoomName() {
			return roomName;
		}
	}
	
	/**
	 * <pre>
	 *  만약 채팅방이 없으면 새로운 채팅방 생성
	 * </pre>
	 * @param roomName
	 */
	public boolean createChatRoom(String roomName) {
		if (ROOMS.containsKey(roomName))
			return false;
		ROOMS.put(roomName, new HashSet<WebSocketSession>());
		return true;
	};

	/**
	 * <pre>
	 *  아직 채팅방에 속하지 않은 사용자라면,
	 *  본인이 속하는 채팅방을 찾아 채팅방에 귀속시킨다.
	 * </pre>
	 * @param user
	 * @param neighborId
	 * @param sender
	 * @param roomName
	 * @throws Exception
	 */
	public boolean joinUser(
				WebSocketSession user
				, Long neighborId
				, String sender
				, String roomName
			) throws Exception {
		if (roomName == null || roomName.trim().equals("") || user == null)
			throw new Exception("알 수 없는 채팅방 또는 사용자입니다: joinUser() Parameter Error");
		Set<WebSocketSession> chatRoom = ROOMS.get(roomName);
		if (chatRoom == null)
			throw new Exception("존재하지 않는 방입니다.");
		else if (chatRoom.contains(user))
			return false;
		chatRoom.add(user);
		CHAT_INFO.put(user, new ChatInfo(neighborId, sender, roomName));
		return true;
	};
	
	/**
	 * <pre>
	 *  세션 정보를 지울 때 사용한다.
	 *  해당 메서드를 사용하지 않고 두 객체를 각각 접근하여 세션을 지울 경우,
	 *  한 객체라도 데이터 삭제에 실패하면 메모리 누수가 발생할 수 있다.
	 * </pre>
	 * @param user
	 * @param neighborMapper
	 * @throws Exception 
	 */
	public void leaveUser(WebSocketSession user, NeighborMapper neighborMapper) throws Exception {
		ChatInfo info = CHAT_INFO.get(user);
		if (info == null) 
			throw new Exception("사용자의 채팅방 정보를 읽을 수 없습니다.: leaveUser");
		
		String roomName = info.getRoomName(); 
		if (roomName == null || roomName.trim().equals("") || user == null)
			throw new Exception("알 수 없는 채팅방 또는 사용자입니다: leaveUser() Parameter Error");

		Set<WebSocketSession> chatRoom = ROOMS.get(roomName);
		chatRoom.remove(user);// 채팅방 나가기
		CHAT_INFO.remove(user);// 채팅방 정보 삭제
		neighborMapper.leaveChatroom(info.getNeighborId());// DB chatroom_active='N'
		if (chatRoom.isEmpty()) {// 채팅방 삭제
			ROOMS.remove(roomName);
			neighborMapper.notifyEndOfChatroom(info.getNeighborId());
		} else {// 상대방에게 특정 사람이 나갔다는 메시지 전송
			chatting(null
					, MessageType.OTHER
					, roomName
					, "채팅 종료"
					, info.getName()
					, info.getName()+"님이 채팅방을 나갔습니다."
			); // 채팅방에 메시지 보내기
		}
	}
	
	/**
	 * <pre>
	 *  유형에 따라 본인 또는 상대방에게 메시지를 전송한다.
	 * <pre>
	 * @param self
	 * @param type
	 * @param roomName
	 * @param time
	 * @param sender
	 * @param content
	 */
	public void chatting(
				WebSocketSession self
				, MessageType type
				, String roomName
				, String time
				, String sender
				, String content
			) {
		try {
			JSONObject msg = new JSONObject();
			msg.put("time", time);
			msg.put("sender", sender);
			msg.put("content", content);
			if (type == MessageType.ME) {
				synchronized (self) {
					self.sendMessage(new TextMessage(msg.toJSONString()));// 본인한테 메시지 보내기					
				} 
			} else if (type == MessageType.OTHER) {
				Set<WebSocketSession> ChatRoom = ROOMS.getOrDefault(roomName, new HashSet());
				for (WebSocketSession user : ChatRoom) {
					if (!user.isOpen() || user.equals(self))
						continue;
					synchronized (user) {
						user.sendMessage(new TextMessage(msg.toJSONString()));
					}
				}// for
			}// if-else
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * <pre>
	 *  본인이 속한 채팅방을 찾고
	 *  없을 경우, 새로운 채팅방 이름을 만들어 반환한다.
	 * </pre>
	 * @param session
	 * @param neighborMapper
	 * @param neighborId
	 * @return
	 * @throws Exception
	 */
	public String getRoomName(
				WebSocketSession session
				, NeighborMapper neighborMapper
				, Long neighborId
			) throws Exception {
		ChatInfo info = CHAT_INFO.get(session);// 등록된 채팅방의 이름
		if (info == null || info.getRoomName() == null) {
			return makeRoomId(neighborMapper, neighborId);// 새로운 채팅방의 이름
		}
		return info.getRoomName();
	}
	
	/**
	 * <pre>
	 *  본인이 속한 채팅방을 찾은 뒤, 이름을 반홚해준다.
	 *  본인 채팅방이 없을 경우, exception을 던진다.
	 * </pre>
	 * @param session
	 * @param neighborId
	 * @return
	 * @throws Exception
	 */
	public String getRoomName(
			WebSocketSession session, Long neighborId) throws Exception {
	ChatInfo info = CHAT_INFO.get(session);// 등록된 채팅방의 이름
	if (info == null || info.getRoomName() == null) {
		throw new Exception("등록되지 않은 채팅방입니다.");
	}
	return info.getRoomName();
}
	
	/**
	 * <pre>
	 *  neighborId를 통해 Database에서 profile_id 값을 구하고,
	 *  이를 바탕으로 roomName을 새로 생성한다. 
	 * </pre>
	 * @param neighborMapper
	 * @param neighborId
	 * @return
	 * @throws Exception
	 */
	private String makeRoomId(NeighborMapper neighborMapper, Long neighborId) throws Exception {
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
