package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.SearchDailyInfoVO;

public interface NotesMapper {

	public int postNote(SearchDailyInfoVO vo);

	public int putNote(SearchDailyInfoVO vo);
	
	public int patchNote(SearchDailyInfoVO vo);

	public int deleteNote(SearchDailyInfoVO vo);

}
