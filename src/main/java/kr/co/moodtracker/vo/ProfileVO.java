package kr.co.moodtracker.vo;

public class ProfileVO {
	private int user_profile_id;
	private String nickname;
	private String imagePath;
	private String description;
	private String sessionStatus;
	private String sharingCalendar;
	
	public int getUser_profile_id() {
		return user_profile_id;
	}
	public void setUser_profile_id(int user_profile_id) {
		this.user_profile_id = user_profile_id;
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
