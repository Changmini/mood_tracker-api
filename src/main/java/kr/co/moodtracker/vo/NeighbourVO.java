package kr.co.moodtracker.vo;

public class NeighbourVO {
	private int neighbour_id;
	private int user_profile_id;
	private String followRequest;
	private String followAccept;
	private String externalAccess;
	
	public int getNeighbour_id() {
		return neighbour_id;
	}
	public void setNeighbour_id(int neighbour_id) {
		this.neighbour_id = neighbour_id;
	}
	public int getUser_profile_id() {
		return user_profile_id;
	}
	public void setUser_profile_id(int user_profile_id) {
		this.user_profile_id = user_profile_id;
	}
	public String getFollowRequest() {
		return followRequest;
	}
	public void setFollowRequest(String followRequest) {
		this.followRequest = followRequest;
	}
	public String getFollowAccept() {
		return followAccept;
	}
	public void setFollowAccept(String followAccept) {
		this.followAccept = followAccept;
	}
	public String getExternalAccess() {
		return externalAccess;
	}
	public void setExternalAccess(String externalAccess) {
		this.externalAccess = externalAccess;
	}
}
