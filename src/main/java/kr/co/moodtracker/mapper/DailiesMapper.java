package kr.co.moodtracker.mapper;

import java.util.List;

import kr.co.moodtracker.vo.DailyEntryVO;
import kr.co.moodtracker.vo.SearchVO;

public interface DailiesMapper {

	public List<DailyEntryVO> getDailyEntryOfTheMonth(DailyEntryVO vo);

	public int postDaily(DailyEntryVO vo);
}
