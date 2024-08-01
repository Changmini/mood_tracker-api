package kr.co.moodtracker.service;

import java.util.Map;

import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.mapper.MoodMapper;

public class MoodSettingService {
	
	MoodMapper moodMapper;
	
	public Map<String,Object> getMoodList() throws SettingDataException {
		Map<String,Object> result = moodMapper.getMoodList();
		if (result == null)
			throw new SettingDataException();
		return result;
	}
	
}
