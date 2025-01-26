package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.DailyInfoVO;

public interface NotesMapper {

	public int postNote(DailyInfoVO vo);

	public int putNote(DailyInfoVO vo);

	public int deleteNote(DailyInfoVO vo);

}
