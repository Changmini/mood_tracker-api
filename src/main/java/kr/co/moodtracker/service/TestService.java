package kr.co.moodtracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.mapper.UsersMapper;

@Service
public class TestService {
	
	@Autowired
	UsersMapper usersMapper;
	
	public void getUser() {
		Map<String, String> vo = new HashMap<String, String>();
		vo.put("username", "test");
		vo.put("password", "test");
		Map<String, Object>result = usersMapper.getUser(vo);
		System.out.println(result);
	}
}
