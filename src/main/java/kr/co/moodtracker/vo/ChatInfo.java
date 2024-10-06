package kr.co.moodtracker.vo;

public class ChatInfo {
	private Long neighborId;
	private String sender;
	private String roomName;
	
	public static class Builder {
		Long neighborId;
		String sender;
		String roomName;
		
		public Builder() {}
		public Builder neighborId(Long val) {
			neighborId = val;
			return this;
		}
		public Builder sender(String val) {
			sender = val;
			return this;
		}
		public Builder roomName(String val) {
			roomName = val;
			return this;
		}
		public ChatInfo build() {
			return new ChatInfo(this);
		}
	}
	
	public ChatInfo() {}
	public ChatInfo(Builder builder) {
		this.neighborId = builder.neighborId;
		this.sender = builder.sender;
		this.roomName = builder.roomName;
	}
	public Long getNeighborId() {
		return neighborId;
	}
	public void setNeighborId(Long neighborId) {
		this.neighborId = neighborId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

}
