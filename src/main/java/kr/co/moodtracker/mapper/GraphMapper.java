package kr.co.moodtracker.mapper;

import java.util.List;
import java.util.Map;

import kr.co.moodtracker.vo.DailySearchVO;

public interface GraphMapper {

	public List<Map<String, Object>> getMoodLevelData(DailySearchVO vo);

}
