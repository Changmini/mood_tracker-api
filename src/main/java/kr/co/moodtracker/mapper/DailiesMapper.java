package kr.co.moodtracker.mapper;

import java.util.List;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.SearchVO;

public interface DailiesMapper {

	public List<DailyInfoVO> getDailyEntryOfTheMonth(DailyInfoVO vo);

	public int postDaily(DailyInfoVO vo);

	public int deleteDaily(DailyInfoVO vo);
	
}
