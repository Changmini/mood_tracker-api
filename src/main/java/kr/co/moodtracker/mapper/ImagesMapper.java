package kr.co.moodtracker.mapper;

import java.util.List;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.SearchDailyInfoVO;

public interface ImagesMapper {
	
	public List<DailyInfoVO> getImageInfoList(SearchDailyInfoVO vo);

	public int postImage(SearchDailyInfoVO vo);

	public int putImage(SearchDailyInfoVO vo);

	public int deleteAllImage(SearchDailyInfoVO vo);
	
	public int deleteImage(SearchDailyInfoVO vo);

}
