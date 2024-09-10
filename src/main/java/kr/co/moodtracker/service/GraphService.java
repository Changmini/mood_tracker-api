package kr.co.moodtracker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class GraphService {

	public Map<?,?> getGraphData(int userId) {
		Map<String, List<Object>> data = new HashMap<>();
		/* 라벨링 */
		List<Object> labels = new ArrayList<Object>() {{
			add("sunday"); add("monday"); add("tuesday");
			add("wednesday"); add("thursday"); add("friday");
			add("saturday");
		}};
		data.put("labels", labels);
		data.put("datasets", null);
		
		return null;
	}
	
}
