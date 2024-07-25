package kr.co.moodtracker.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class MoodMapper {

	private Map<Integer, String> mood = new HashMap<>();
	
    public MoodMapper() {
    	mood.put(100, "환희");
        mood.put(85, "기쁨");
        mood.put(70, "만족");
        mood.put(50, "보통");
        mood.put(30, "우울");
        mood.put(15, "슬픔");
        mood.put(0, "절망");
    }
    
    public Map<Integer, String> getMood() {
		return mood;
	}

}
