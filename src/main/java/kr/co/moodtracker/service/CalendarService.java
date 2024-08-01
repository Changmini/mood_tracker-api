package kr.co.moodtracker.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.mapper.DailiesMapper;
import kr.co.moodtracker.mapper.MoodsMapper;
import kr.co.moodtracker.mapper.NotesMapper;
import kr.co.moodtracker.vo.DailyEntryVO;

@Service
public class CalendarService {
	
	@Autowired
	DailiesMapper dailiesMapper;
	@Autowired
	NotesMapper notesMapper;
	@Autowired
	MoodsMapper moodsMapper;
	
	public List<DailyEntryVO> getDailyEntryOfTheMonth(DailyEntryVO vo) throws DataMissingException {
		DateHandler.determineDateRange(vo);
		vo.setUserId(1);
		List<DailyEntryVO> dailies = dailiesMapper.getDailyEntryOfTheMonth(vo);
		List<DailyEntryVO> dailyEntryList = DateHandler.makeDateList(vo, dailies);
		if (dailyEntryList.size() < 1) return Collections.emptyList();
		return dailyEntryList;
	}

	public void postDailyEntry(DailyEntryVO vo) throws DataNotInsertedException {
		// 트랜잭션을 사용해서 하나라도 실패하면 롤백하는 코드를 작성하는 것이 좋아보임.
		int cnt = dailiesMapper.postDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("날짜 정보를 등록 실패했습니다.");
		cnt = notesMapper.postNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("텍스트 정보를 등록 실패했습니다.");
		cnt = moodsMapper.postMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 등록 실패했습니다.");
	}
	
}// class
