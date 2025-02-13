package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO extends ProfileVO {

	private int userId;
	private String username;
	private String password;
	private String email;
}
