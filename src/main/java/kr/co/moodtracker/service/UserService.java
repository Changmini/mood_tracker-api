package kr.co.moodtracker.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.mapper.UsersMapper;
import kr.co.moodtracker.vo.UserVO;

@Service
public class UserService {
	
	@Autowired
	UsersMapper userMapper;
	
	public UserVO getUser(Map<String, String> vo) {
		String username = vo.get("username");
		String password = vo.get("password");
		boolean emptyUsername = !(username != null && !username.equals("")); 
		boolean emptyPassword = !(password != null && !password.equals("")); 
		if (emptyUsername || emptyPassword) 
			return null;
		UserVO user = userMapper.getUser(vo);
		if (!(user != null && user.getUserId() > 0))
			return null;
		return user;
	}
	
}
