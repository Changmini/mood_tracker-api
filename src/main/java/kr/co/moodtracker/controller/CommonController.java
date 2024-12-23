package kr.co.moodtracker.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;

abstract class CommonController {
	
	protected void setUserId(HttpSession session, DailySearchVO vo
			) throws SessionNotFoundException {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected void setUserId(HttpSession session, DailyInfoVO vo
			) throws SessionNotFoundException {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected void setUserId(HttpSession session, SearchNeighborVO vo
			) throws SessionNotFoundException {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected int getUserId(HttpSession session
			) throws SessionNotFoundException {
		UserVO u = getAttributeUser(session);
		return u.getUserId();
	}
	
	protected UserVO getUserInfo(HttpSession session
			) throws SessionNotFoundException {
		UserVO u = getAttributeUser(session);
		return u;
	}
	
	private UserVO getAttributeUser(HttpSession session) {
		return (UserVO) session.getAttribute("USER");
	}
}
