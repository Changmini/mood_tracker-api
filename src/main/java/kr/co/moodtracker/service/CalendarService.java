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
		vo.setUserId(1);
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
		if (cnt == 0) throw new DataNotInsertedException("텍스트 정보를 등록 실패했습니다.");
		cnt = moodsMapper.postMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 등록 실패했습니다.");
		cnt = dailiesMapper.postDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("날짜 정보를 등록 실패했습니다.");
	}
	
}// class
