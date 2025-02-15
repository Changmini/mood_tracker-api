package kr.co.moodtracker.mapper;

import java.util.List;
import java.util.Map;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.SearchDailyInfoVO;

public interface DailiesMapper {

	public List<DailyInfoVO> getDailyInfoOfTheMonth(SearchDailyInfoVO vo);

	public List<DailyInfoVO> getDailyInfoList(SearchDailyInfoVO vo);
	
	public List<Map<String,Object>> getDailyInfoCount(SearchDailyInfoVO vo);
	
	public DailyInfoVO getDailyInfo(SearchDailyInfoVO vo);
	
	/**
	 * 날짜로 dailyId, noteId, moodId 세팅
	 * @param vo
	 */
	public DailyInfoVO getDailyRelatedIds (SearchDailyInfoVO vo);
	
	public int checkDailyInfo(SearchDailyInfoVO vo);
	
	public int postDaily(SearchDailyInfoVO vo);

	public int deleteDaily(SearchDailyInfoVO vo);


	
}
