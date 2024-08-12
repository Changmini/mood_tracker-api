package kr.co.moodtracker.mapper;

import java.util.List;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;

public interface DailiesMapper {

	public List<DailyInfoVO> getDailyInfoOfTheMonth(DailySearchVO vo);

	public List<DailyInfoVO> getDailyInfoList(DailySearchVO vo);
	
	public int postDaily(DailyInfoVO vo);

	public int deleteDaily(DailyInfoVO vo);

	
}
