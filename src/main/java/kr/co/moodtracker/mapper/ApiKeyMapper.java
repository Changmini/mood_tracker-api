package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.UserVO;

public interface ApiKeyMapper {

	public int validateApiKey(String key);
	
	public String getApiKey(int userId);
	
	public int updateApiKey(UserVO vo);

}
