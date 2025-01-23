package kr.co.moodtracker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.moodtracker.controller.ExceptionController;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.vo.UserVO;

public class SessionCheckHandler implements HandlerInterceptor {
	
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
	) throws SessionNotFoundException 
	{
		// OPTIONS 요청은 로그인 확인을 건너뛴다.
	    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
	        return true;
	    }
	    
		UserVO u = (UserVO) req.getSession().getAttribute("USER");
		if (u == null) {
			logger.info("User not logged in(비로그인 접속)");
			/**
			 * 예외를 던지면 CrossOrigin 문제가 발생할 수 있음.
			 * 이유) Controller에서 response가 request 헤더를 보고 세팅이 되는데
			 *      예외처리로 인해 Controller로 도달하지 못하면서 문제가 발생함.
			 *      
			 *      그래서 [return false VS 예외처리]를 고민해 보길...
			 */
			throw new SessionNotFoundException("로그인이 필요합니다.");
		}
		return true;
	}
	
}
