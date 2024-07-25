package kr.co.moodtracker.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ko.co.moodtracker.vo.DailyEntryVO;
import ko.co.moodtracker.vo.SearchVO;
import kr.co.moodtracker.dao.MoodMapper;
import kr.co.moodtracker.exception.DataMissingException;

@Service
public class CalendarService {
	
	@Autowired
	MoodMapper moodMapper;
	
	public DailyEntryVO getDailybox(SearchVO vo) {
//		Dailybox dailybox = repository.getDailybox();
		return null;
	}
	
	public List<DailyEntryVO> getDailyboxOfTheMonth(SearchVO vo) throws DataMissingException {
		DateHandler.determineDateRange(vo);
		List<String> dateList = DateHandler.makeDateList(vo);               // 임시 날짜 생성
		List<DailyEntryVO> dailyEntryList = testDailybox(dateList);// 임시 날짜에 내용 저장
//		List<Dailybox> dailyEntryList = repository.getDailyboxOfTheMonth();
		if (dailyEntryList.size() < 1) return Collections.emptyList();
		return dailyEntryList;
	}
	
	
	private List<DailyEntryVO> testDailybox(List<String> dateList) {
		List<DailyEntryVO> list = new ArrayList<DailyEntryVO>();
		Map<Integer, String> mood = moodMapper.getMood();
		List<Integer> li = new ArrayList<>(mood.keySet());
		Random rand = new Random();
		int size = dateList.size(); 
		for (int i=0; i<size; i++) {
			DailyEntryVO d = new DailyEntryVO();
			d.setDate(dateList.get(i));
			d.setTitle("오늘의 기분은.." + i);
			d.setNotes("이런이런 일이 있었다..." + i);
			int j = rand.nextInt(li.size());
			d.setMood(li.get(j));
			list.add(d);
		} 
		return list;
	}
	
}// class
