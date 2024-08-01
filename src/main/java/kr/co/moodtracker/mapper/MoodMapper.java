package kr.co.moodtracker.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface MoodMapper {

	public Map<String, Object> getMoodList();

}
