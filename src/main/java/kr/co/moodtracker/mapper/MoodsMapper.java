package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.DailyInfoVO;

public interface MoodsMapper {

	public int postMood(DailyInfoVO vo);

	public int patchMood(DailyInfoVO vo);

	public int deleteMood(DailyInfoVO vo);

}
