package kr.co.moodtracker.vo;

public class ChatInfo {
	private String roomName;
	private String sender;
	
	public static class Builder {
		String roomName;
		String sender;
		
		public Builder() {}
		public Builder roomName(String val) {
			roomName = val;
			return this;
		}
		public Builder sender(String val) {
			sender = val;
			return this;
		}
		public ChatInfo build() {
			return new ChatInfo(this);
		}
	}
	
	public ChatInfo() {}
	public ChatInfo(Builder builder) {
		this.roomName = builder.roomName;
		this.sender = builder.sender;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

}
