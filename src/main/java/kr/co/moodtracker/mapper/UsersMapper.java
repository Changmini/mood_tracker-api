package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.ProfileVO;
import kr.co.moodtracker.vo.UserVO;

public interface UsersMapper {

	public UserVO findByUsername(String username);

	public void postUser(UserVO vo);
	
	public void postUserProfile(UserVO vo);
	
	public ProfileVO getUserProfile(int userId);

	public int patchUserProfile(UserVO vo);

	public int putUserProfileImage(UserVO vo);
	
	public int getUserProfileId(String nickname);

}
