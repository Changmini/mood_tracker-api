package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.SearchDailyInfoVO;

public interface MoodsMapper {

	public int postMood(SearchDailyInfoVO vo);

	public int putMood(SearchDailyInfoVO vo);

	public int deleteMood(SearchDailyInfoVO vo);

}
