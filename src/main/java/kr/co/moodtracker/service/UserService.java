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
	
	/**
	 * <pre>
	 * 	인자 값은 username과 password를 key로 하고 <br/>
	 * 	해당 key와 의미가 일치하는 적절한 value를 저장하여 넘겨주면 <br/>
	 * 	사용자 정보를 리턴해준다.
	 * </pre>
	 * @param vo
	 * @return custom ValueObject
	 */
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
