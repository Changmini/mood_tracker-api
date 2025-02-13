package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchNeighborVO extends SearchVO {
	
	private int neighborId;
	private int hostProfileId;
	private int guestProfileId;
	private String requester;
	private String synchronize;
	private String chatroomActive;
	private String calExtAccess;
	private String createdAt;
	private String updatedAt;
	/* 프로필 정보 */
	private String nickname;
	/* 유저 정보 */
	private int userId;

}
