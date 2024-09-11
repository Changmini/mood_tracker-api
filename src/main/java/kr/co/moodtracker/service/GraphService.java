package kr.co.moodtracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.mapper.GraphMapper;
import kr.co.moodtracker.vo.DailySearchVO;

@Service
public class GraphService {
	
	@Autowired
	GraphMapper graphMapper;

	public Map<?,?> getMoodLevelData(int userId, DailySearchVO vo) {
		vo.setUserId(userId);
		Map<String, List<Object>> graphData = new HashMap<>();
		/* 라벨링 */
		List<Object> labels = new ArrayList<Object>() {{
			add("sunday"); add("monday"); add("tuesday");
			add("wednesday"); add("thursday"); add("friday");
			add("saturday");
		}};
		graphData.put("labels", labels);
		
		/* 데이터셋 */		
		List<Map<String,Object>> metadata = graphMapper.getMoodLevelData(vo);
		List<Object> datasets = new ArrayList<>();
		Map<String, Object> dataset = null;
		Integer[] data = null;
		String preLabel = "";
		for (Map<String,Object> m : metadata) {
			String label = (String) m.get("label");
			/* 1~7 = 일요일~토요일 */
			Integer weekNum = Integer.valueOf(m.get("weekNum").toString());
			Integer level = Integer.valueOf(m.get("moodLevel").toString());
			if (!label.equals(preLabel)) {
				preLabel = label;// 이전 라벨과 현재 라벨 비교
				data = new Integer[7];
				Integer tmpLevel = data[weekNum-1];
				tmpLevel = ((tmpLevel != null ? tmpLevel : 0) + level);
				data[weekNum-1] = tmpLevel;
				dataset = new HashMap<>();
				dataset.put("label", label); // month (월)
				dataset.put("data", data); // 요일별 mood_level 합산값
				datasets.add(dataset);
			}
			/* mood_level 합산 */
			Integer tmpLevel = data[weekNum-1];
			tmpLevel = ((tmpLevel != null ? tmpLevel : 0) + level);
			data[weekNum-1] = tmpLevel;
		}
		graphData.put("datasets", datasets);
		return graphData;
	}
	
}
