package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	
}
