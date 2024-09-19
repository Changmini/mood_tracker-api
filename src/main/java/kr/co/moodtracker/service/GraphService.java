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
		Map<String, Map<?,?>> graphData = new HashMap<>();
		Map<String, Object> lineChart = new HashMap<>();
		Map<String, Object> barChart = new HashMap<>();
		/* 라벨링 */
		lineChart.put("labels", DateHandler.dayOfWeeks);
		barChart.put("labels", DateHandler.dayOfMonth);
		
		/* 데이터셋 */		
		List<Map<String,Object>> metadata = graphMapper.getMoodLevelData(vo);
		List<Object> lineDatasets = new ArrayList<>();
		Map<String, Object> lineDataset = null;
		int[] lineData = null;
		List<Object> barDatasets = new ArrayList<>();
		Map<String, Object> barDataset = null;
		int[] barData = null;
		String preLabel = "";
		for (Map<String,Object> m : metadata) {
			String label = (String) m.get("label");
			/* 1~7 = 일요일~토요일 */
			Integer weekNum = Integer.valueOf(m.get("weekNum").toString());
			Integer dayOfMonth = Integer.valueOf(m.get("dayOfMonth").toString());
			Integer level = Integer.valueOf(m.get("moodLevel").toString());
			if (!label.equals(preLabel)) {
				/* common */
				preLabel = label;// 이전 라벨과 현재 라벨 비교
				/* Line chart */
				lineData = new int[7];
				lineDataset = new HashMap<>();
				lineDataset.put("label", label); // month (월)
				lineDataset.put("data", lineData); // 요일별 mood_level 합산값
				lineDatasets.add(lineDataset);
				/* Bar chart */
				barData = new int[31];
				barDataset = new HashMap<>();
				barDataset.put("label", label); // month (월)
				barDataset.put("data", barData);
				barDatasets.add(barDataset);
			}
			/* Line - mood_level 합산 */
			lineData[weekNum-1] = lineData[weekNum-1] + level;
			/* bar */
			barData[dayOfMonth-1] = level;
		}
		lineChart.put("datasets", lineDatasets);
		barChart.put("datasets", barDatasets);
		graphData.put("lineChart", lineChart);
		graphData.put("barChart", barChart);
		return graphData;
	}
	
}
