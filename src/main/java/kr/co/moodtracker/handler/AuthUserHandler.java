package kr.co.moodtracker.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.InvalidApiKeyException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;

public class AuthUserHandler {
	static final private TextEncryptor enc;
	static final private JSONParser jp;
	
	static {
		enc = Encryptors.text("mood-sec-tr-ack-er", "1235679abcdefABC");
		jp = new JSONParser();
	}
	
	/**
	 * 새로운 API Key 생성
	 * @param userId
	 * @return
	 */
	static public String generateApiKey(int userId) {
		JSONObject jo = new JSONObject();
		jo.put("id", userId);
		return enc.encrypt(jo.toJSONString());
    }
	
	/**
	 * API Key를 파싱하여, userId 추출하는 메서드
	 * @param apiKey
	 * @return
	 * @throws InvalidApiKeyException
	 */
	static private int getUserId(String apiKey) throws InvalidApiKeyException {
		String decoding = enc.decrypt(apiKey);
		JSONObject jo;
		try {
			jo = (JSONObject) jp.parse(decoding);
		} catch (ParseException e) {
			throw new InvalidApiKeyException("올바르지 않은 API 형식입니다.");
		}
		return Integer.valueOf(jo.get("id").toString());
	}
	/**
	 * API Key의 내포된 userId 값을 전달된 객체에 세팅
	 * @param apiKey
	 * @param vo
	 * @throws InvalidApiKeyException
	 */
	static public void setUserId(
			String apiKey
			, Object vo
	) throws InvalidApiKeyException {
		int userId = getUserId(apiKey);
		if (vo instanceof DailySearchVO) {
			((DailySearchVO) vo).setUserId(userId); 
		} else if (vo instanceof DailyInfoVO) {
			((DailyInfoVO) vo).setUserId(userId); 
		} else if (vo instanceof SearchNeighborVO) {
			((SearchNeighborVO) vo).setUserId(userId); 
		} else if (vo instanceof UserVO) {
			((UserVO) vo).setUserId(userId); 
		}
	}
	
	
	/**
	 * Sesstion에서 USER 속성 반환 메서드
	 * @param session
	 * @return
	 */
	static private UserVO getAttributeUser(HttpSession session) {
		return (UserVO) session.getAttribute("USER");
	}
	/**
	 * Session의 User 정보를
	 * vo 객체에 넣어주는 메서드 
	 * @param session
	 * @param vo
	 */
	static public void setUserId(
			HttpSession session
			, Object vo
	) {
		UserVO u = getAttributeUser(session);
		if (vo instanceof DailySearchVO) {
			((DailySearchVO) vo).setUserId(u.getUserId()); 
		} else if (vo instanceof DailyInfoVO) {
			((DailyInfoVO) vo).setUserId(u.getUserId()); 
		} else if (vo instanceof SearchNeighborVO) {
			((SearchNeighborVO) vo).setUserId(u.getUserId()); 
		} else if (vo instanceof UserVO) {
			((UserVO) vo).setUserId(u.getUserId()); 
		}
	}
	/**
	 * Session의 User 정보 중 id 값을 반환하는 메서드
	 * @param session
	 * @return
	 */
	static public int getUserId(
			HttpSession session
	) {
		UserVO u = getAttributeUser(session);
		return u.getUserId();
	}
	/**
	 * Session의 User 정보를 반환하는 메서드
	 * @param session
	 * @return
	 */
	static public UserVO getUserInfo(
			HttpSession session
	) {
		return getAttributeUser(session);
	}
	
}
