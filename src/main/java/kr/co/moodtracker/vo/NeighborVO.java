package kr.co.moodtracker.vo;

public class NeighborVO extends ProfileVO {
	private int neighborId;
	private int hostProfileId;
	private int guestProfileId;
	private String requester;
	private String synchronize;
	private String chatroomActive;
	private String calExtAccess;
	private String createdAt;
	private String updatedAt;
	
	public int getNeighborId() {
		return neighborId;
	}
	public void setNeighborId(int neighborId) {
		this.neighborId = neighborId;
	}
	public int getHostProfileId() {
		return hostProfileId;
	}
	public void setHostProfileId(int hostProfileId) {
		this.hostProfileId = hostProfileId;
	}
	public int getGuestProfileId() {
		return guestProfileId;
	}
	public void setGuestProfileId(int guestProfileId) {
		this.guestProfileId = guestProfileId;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getSynchronize() {
		return synchronize;
	}
	public void setSynchronize(String synchronize) {
		this.synchronize = synchronize;
	}
	public String getChatroomActive() {
		return chatroomActive;
	}
	public void setChatroomActive(String chatroomActive) {
		this.chatroomActive = chatroomActive;
	}
	public String getCalExtAccess() {
		return calExtAccess;
	}
	public void setCalExtAccess(String calExtAccess) {
		this.calExtAccess = calExtAccess;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
