package kr.co.moodtracker.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;

public class CommonController {
	/**
	 * <pre>로그인 상태 확인</pre>
	 * @param session
	 * @param vo
	 * @throws SessionNotFoundException
	 */
	protected void setUserInfo(HttpSession session, DailySearchVO vo
			) throws SessionNotFoundException {
		UserVO user = (UserVO) session.getAttribute("USER");
		checkNull(user);
		vo.setUserId(user.getUserId());
	}
	
	protected void setUserInfo(HttpSession session, DailyInfoVO vo
			) throws SessionNotFoundException {
		UserVO user = (UserVO) session.getAttribute("USER");
		checkNull(user);
		vo.setUserId(user.getUserId());
	}
	
	protected void setUserInfo(HttpSession session, SearchNeighborVO vo
			) throws SessionNotFoundException {
		UserVO user = (UserVO) session.getAttribute("USER");
		checkNull(user);
		vo.setUserId(user.getUserId());
	}
	
	protected UserVO setUserInfo(HttpSession session
			) throws SessionNotFoundException {
		UserVO user = (UserVO) session.getAttribute("USER");
		checkNull(user);
		return user;
	}
	
	private void checkNull(UserVO u) throws SessionNotFoundException {
		if (u != null)
			return ;
		throw new SessionNotFoundException("로그인이 필요합니다.");
	}
}
