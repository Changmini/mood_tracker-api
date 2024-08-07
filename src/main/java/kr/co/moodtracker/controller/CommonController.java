package kr.co.moodtracker.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.UserVO;

public class CommonController {
	
	protected void setUserInfo(HttpSession s, DailyInfoVO vo
			) throws SessionNotFoundException {
		UserVO user = (UserVO) s.getAttribute("USER");
		if (user == null)
			throw new SessionNotFoundException("로그인이 필요합니다.");
		vo.setUserId(user.getUserId());
	}

}
