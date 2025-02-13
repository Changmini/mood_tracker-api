package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileVO extends ApiKeyVO {
	
	private int userProfileId;
	private String nickname;
	private String imagePath;
	private String description;
	private String sessionStatus;
	private String sharingCalendar;
	
}
