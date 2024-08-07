package kr.co.moodtracker.mapper;

import kr.co.moodtracker.vo.DailyInfoVO;

public interface NotesMapper {

	public int postNote(DailyInfoVO vo);

	public int patchNote(DailyInfoVO vo);

	public int deleteNote(DailyInfoVO vo);

}
