package kr.co.moodtracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.handler.DateHandler;
import kr.co.moodtracker.mapper.GraphMapper;
import kr.co.moodtracker.vo.ChartVO;
import kr.co.moodtracker.vo.DailySearchVO;

@Service
public class GraphService {
	
	@Autowired
	GraphMapper graphMapper;

	public Map<?,?> getMoodLevelData(DailySearchVO vo) {
		Map<String, Map<?,?>> graphData = new HashMap<>();
		/* 라벨링 */
		ChartVO line = new ChartVO(DateHandler.DAY_OF_WEEKS);
		ChartVO bar = new ChartVO(DateHandler.DAY_OF_MONTH);
		
		/* 데이터셋 */		
		List<Map<String,Object>> metadata = graphMapper.getMoodLevelData(vo);
		String preLabel = "";
		for (Map<String,Object> m : metadata) {
			String label = (String) m.get("label");// yyyy-mm
			Integer weekNum = Integer.valueOf(m.get("weekNum").toString());// 1~7 (=일요일~토요일)
			Integer dayOfMonth = Integer.valueOf(m.get("dayOfMonth").toString());// 1~31 (=일)
			Integer level = Integer.valueOf(m.get("moodLevel").toString());// mood level
			if (!label.equals(preLabel)) {// 이전 라벨과 현재 라벨 비교
				preLabel = label;// common
				line.setElement(label, 7);
				bar.setElement(label, 31);
			}
			/* Line - mood_level 합산 */
			int [] l = line.getData();
			l[weekNum-1] = l[weekNum-1] + level;
			/* bar */
			int [] b = bar.getData();
			b[dayOfMonth-1] = level;
		}
		graphData.put("lineChart", line.getChartData());
		graphData.put("barChart", bar.getChartData());
		return graphData;
	}
	
}
