package kr.co.moodtracker.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.mapper.DailiesMapper;
import kr.co.moodtracker.mapper.MoodsMapper;
import kr.co.moodtracker.mapper.NotesMapper;
import kr.co.moodtracker.vo.DailyInfoVO;

@Service
public class CalendarService {
	
	@Autowired
	DailiesMapper dailiesMapper;
	@Autowired
	NotesMapper notesMapper;
	@Autowired
	MoodsMapper moodsMapper;
	
	public List<DailyInfoVO> getDailyEntryOfTheMonth(DailyInfoVO vo) throws DataMissingException {
		DateHandler.determineDateRange(vo);
		List<DailyInfoVO> dailies = dailiesMapper.getDailyEntryOfTheMonth(vo);
		List<DailyInfoVO> dailyEntryList = DateHandler.makeDateList(vo, dailies);
		if (dailyEntryList.size() < 1) return Collections.emptyList();
		return dailyEntryList;
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void postDailyInfo(DailyInfoVO vo) throws DataNotInsertedException {
		/* 
		 * 이미 데이터가 존재하는지 확인하는 로직을 추가해야 한다.
		 * */
		int cnt = notesMapper.postNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("입력된 텍스트 정보를 확인해주세요.");
		cnt = moodsMapper.postMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 등록 실패했습니다. 등록 데이터: "+cnt);
		cnt = dailiesMapper.postDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("날짜 정보를 등록 실패했습니다. 등록 데이터: "+cnt);
	}
	
	public void patchDailyInfo(DailyInfoVO vo) throws DataNotInsertedException {
		int cnt = notesMapper.patchNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("입력된 텍스트 정보를 확인해주세요.");
		cnt = moodsMapper.patchMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 갱신 실패했습니다. 변경 데이터: "+cnt);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void deleteDailyInfo(DailyInfoVO vo) throws DataNotInsertedException {
		int cnt = notesMapper.deleteNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 noteId 정보가 일치한지 확인이 필요합니다.");
		cnt = moodsMapper.deleteMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 moodId 정보가 일치한지 확인이 필요합니다.");
		cnt = dailiesMapper.deleteDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 dailyId 정보가 일치한지 확인이 필요합니다.");
	}
	
}// class
