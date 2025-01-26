package kr.co.moodtracker.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.mapper.MoodMapper;

public class MoodSettingService {
	
	MoodMapper moodMapper;
	
	@Transactional(readOnly = true)
	public Map<String,Object> getMoodList() throws SettingDataException {
		Map<String,Object> result = moodMapper.getMoodList();
		if (result == null)
			throw new SettingDataException();
		return result;
	}
	
}
