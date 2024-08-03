package kr.co.moodtracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.mapper.NotesMapper;
import kr.co.moodtracker.mapper.UsersMapper;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.UserVO;

@Service
public class TestService {
	
	@Autowired
	UsersMapper usersMapper;
	@Autowired
	NotesMapper notesMapper;
	
	public void getUser() {
		Map<String, String> vo = new HashMap<String, String>();
		vo.put("username", "test");
		vo.put("password", "test");
		UserVO result = usersMapper.getUser(vo);
		System.out.println(result.toString());
	}
	
	public void getNotes() {
		DailyInfoVO d = new DailyInfoVO();
		d.setUserId(1);
		d.setNoteTitle("test");
		d.setNoteContent("testtest");
		int m = notesMapper.postNote(d);
		System.out.println(m);
	}
}
