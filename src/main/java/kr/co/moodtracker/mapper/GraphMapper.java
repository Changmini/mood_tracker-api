package kr.co.moodtracker.mapper;

import java.util.List;
import java.util.Map;

import kr.co.moodtracker.vo.SearchDailyInfoVO;

public interface GraphMapper {

	public List<Map<String, Object>> getMoodLevelData(SearchDailyInfoVO vo);

}
