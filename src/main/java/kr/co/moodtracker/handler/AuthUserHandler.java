package kr.co.moodtracker.handler;

import java.security.SecureRandom;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;

public class AuthUserHandler {
	static final private TextEncryptor enc = Encryptors.text("mood-sec-tr-ack-er", KeyGenerators.string().generateKey());
	static final private SecureRandom secRandom = new SecureRandom();
	static final private JSONParser jp = new JSONParser();
	
	static public String generateApiKey(int id, String username) {
		/* 랜덤토큰 만들기 */
		byte[] tokenBytes = new byte[16];
		secRandom.nextBytes(tokenBytes);
		String token = Base64.getEncoder().encodeToString(tokenBytes);
		
		/* 사용자 정보 */
		JSONObject jo = new JSONObject();
		jo.put("token", token);
		jo.put("id", id);
		
		return enc.encrypt(jo.toJSONString());
    }
	
	static public int getUserId(String apiKey) throws ParseException {
		String decoding = enc.decrypt(apiKey);
		JSONObject jo = (JSONObject) jp.parse(decoding);
		return Integer.valueOf(jo.get("id").toString());
	}
	
	/**
	 * Session의 User 정보를
	 * vo 객체에 넣어주는 메서드 
	 * @param session
	 * @param vo
	 */
	static public void setUserId(
			HttpSession session
			, DailySearchVO vo
	) {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	static public void setUserId(
			HttpSession session
			, DailyInfoVO vo
	) {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
	}
	static public void setUserId(
			HttpSession session
			, SearchNeighborVO vo
	) {
		UserVO u = getAttributeUser(session);
		vo.setUserId(u.getUserId());
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
	static private UserVO getAttributeUser(HttpSession session) {
		return (UserVO) session.getAttribute("USER");
	}
}
