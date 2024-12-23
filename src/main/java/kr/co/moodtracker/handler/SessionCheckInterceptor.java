package kr.co.moodtracker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.moodtracker.controller.ExceptionController;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.UserVO;

public class SessionCheckInterceptor implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	/**
	 * <pre>로그인 상태 확인</pre>
	 * @param session
	 * @param vo
	 * @throws SessionNotFoundException
	 */
	@Override
	public boolean preHandle(
			HttpServletRequest req
			, HttpServletResponse res
			, Object handler
	) throws Exception 
	{
		// OPTIONS 요청은 로그인 확인을 건너뛴다.
	    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
	        return true;
	    }
	    
		UserVO u = (UserVO) req.getSession().getAttribute("USER");
		if (u == null) {
			logger.info("User not logged in(비로그인 접속)");
			throw new SessionNotFoundException("로그인이 필요합니다.");
		}
		return true;
	}
	
}
