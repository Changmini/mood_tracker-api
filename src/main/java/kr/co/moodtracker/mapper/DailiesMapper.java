package kr.co.moodtracker.mapper;

import java.util.List;
import java.util.Map;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;

public interface DailiesMapper {

	public List<DailyInfoVO> getDailyInfoOfTheMonth(DailySearchVO vo);

	public List<DailyInfoVO> getDailyInfoList(DailySearchVO vo);
	
	public List<Map<String,Object>> getDailyInfoCount(DailySearchVO vo);
	
	/**
	 * 날짜로 dailyId, noteId, moodId 세팅
	 * @param vo
	 */
	public DailyInfoVO getDailyRelatedIds (DailyInfoVO vo);
	
	public int checkDailyInfo(DailyInfoVO vo);
	
	public int postDaily(DailyInfoVO vo);

	public int deleteDaily(DailyInfoVO vo);

	
}
