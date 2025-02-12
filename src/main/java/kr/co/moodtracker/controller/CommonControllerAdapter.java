package kr.co.moodtracker.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;

abstract class CommonControllerAdapter {
	
	protected void setUserId(HttpSession session, DailySearchVO vo) 
	{
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected void setUserId(HttpSession session, DailyInfoVO vo) 
	{
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected void setUserId(HttpSession session, SearchNeighborVO vo) 
	{
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	
	protected int getUserId(HttpSession session) 
	{
		UserVO u = getAttributeUser(session);
		return u.getUserId();
	}
	
	protected UserVO getUserInfo(HttpSession session) 
	{
		return getAttributeUser(session);
	}
	
	private UserVO getAttributeUser(HttpSession session) {
		return (UserVO) session.getAttribute("USER");
	}
}
