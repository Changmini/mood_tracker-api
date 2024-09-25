package kr.co.moodtracker.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.moodtracker.vo.ProfileVO;
import kr.co.moodtracker.vo.UserVO;

@Repository
public interface UsersMapper {

	public UserVO getUser(Map<String, String> vo);

	public void postUser(UserVO vo);
	
	public void postUserProfile(UserVO vo);
	
	public ProfileVO getUserProfile(int userId);

	public int patchUserProfile(UserVO vo);

	public int putUserProfileImage(UserVO vo);

}
