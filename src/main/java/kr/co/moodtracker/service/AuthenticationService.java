package kr.co.moodtracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.exception.InvalidApiKeyException;
import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.handler.AuthUserHandler;
import kr.co.moodtracker.mapper.ApiKeyMapper;
import kr.co.moodtracker.vo.UserVO;

@Service
public class AuthenticationService {
	
	@Autowired
	ApiKeyMapper apiKeyMapper;
	
	private final Map<String, Long> apiKeyAndTime = new HashMap<>();
	
	public String getApiKey(UserVO user) {
		return apiKeyMapper.getApiKey(user.getUserId());
	}
	
	public String generateApiKey(UserVO vo) throws SettingDataException {
		if (vo.getKey() != null && apiKeyAndTime.containsKey(vo.getKey())) {
			apiKeyAndTime.remove(vo.getKey());
		}
		String apiToken = AuthUserHandler.generateApiKey(vo.getUserId());
		vo.setKey(apiToken);
		int cnt = apiKeyMapper.updateApiKey(vo);
		if (cnt == 0) throw new SettingDataException("API Key 발급 과정에서 문제가 발생했습니다.");
		
		return apiToken;
	}
	
	public void validateApiKey(String key, Object vo) throws InvalidApiKeyException {
		if (apiKeyAndTime.containsKey(key)) {
			AuthUserHandler.setUserId(key, vo);
			return ;
		}
		int res = apiKeyMapper.validateApiKey(key);
		if (res == 0) throw new InvalidApiKeyException("올바르지 않은 API Key 입니다.");
		apiKeyAndTime.put(key, System.currentTimeMillis() + 1800000);
		AuthUserHandler.setUserId(key, vo);
	}
	
	/**
	 * 30분이 지난 API Key를 apiKeyAndTime에서 제외시키는 단일스레드
	 */
	@Scheduled(fixedRate = 600000)
	private void scheduledTask() {
		for (Map.Entry<String, Long> o : apiKeyAndTime.entrySet()) {
			if (o.getValue() < System.currentTimeMillis())
				apiKeyAndTime.remove(o.getKey());
		}
	}
	
}
