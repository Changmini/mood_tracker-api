package kr.co.moodtracker.mapper;

import java.util.List;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;

public interface ImagesMapper {
	
	public List<DailyInfoVO> getImageInfoList(DailySearchVO vo);

	public int postImage(DailyInfoVO vo);

	public int patchImage(DailyInfoVO vo);

	public int deleteAllImage(DailyInfoVO vo);
	
	public int deleteImage(DailyInfoVO vo);

}
