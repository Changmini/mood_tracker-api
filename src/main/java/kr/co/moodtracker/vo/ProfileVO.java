package kr.co.moodtracker.vo;

public class ProfileVO {
	private int userProfileId;
	private String nickname;
	private String imagePath;
	private String description;
	private String sessionStatus;
	private String sharingCalendar;
	
	public int getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(int userProfileId) {
		this.userProfileId = userProfileId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSessionStatus() {
		return sessionStatus;
	}
	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	public String getSharingCalendar() {
		return sharingCalendar;
	}
	public void setSharingCalendar(String sharingCalendar) {
		this.sharingCalendar = sharingCalendar;
	}
}
