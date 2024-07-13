package kr.co.moodtracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ko.co.moodtracker.vo.DailyEntryVO;
import ko.co.moodtracker.vo.SearchVO;

@Service
public class CalendarService {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String[] dayOfWeeks = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
	
	public DailyEntryVO getDailybox(SearchVO vo) {
//		Dailybox dailybox = repository.getDailybox();
		return null;
	}
	
	public List<DailyEntryVO> getDailyboxOfTheMonth(SearchVO vo) {
		determineDateRange(vo);
		List<String> dateList = getDateList(vo);               // 임시 날짜 생성
		List<DailyEntryVO> dailyEntryList = testDailybox(dateList);// 임시 날짜에 내용 저장
//		List<Dailybox> dailyEntryList = repository.getDailyboxOfTheMonth();
		if (dailyEntryList.size() < 1) return Collections.emptyList();
		return dailyEntryList;
	}
	
	private List<String> getDateList(SearchVO vo) {
		LocalDate s = LocalDate.parse(vo.getStartDate());
		LocalDate e = LocalDate.parse(vo.getEndDate());
		List<String> dateList = new ArrayList<>();
		LocalDate tempDate = s; 
		while(!tempDate.isAfter(e)) {
			dateList.add(tempDate.format(formatter));
			tempDate = tempDate.plusDays(1);
		}
		return dateList;
	}
	private List<DailyEntryVO> testDailybox(List<String> dateList) {
		List<DailyEntryVO> list = new ArrayList<DailyEntryVO>();
		int size = dateList.size(); 
		for (int i=0; i<size; i++) {
			DailyEntryVO d = new DailyEntryVO();
			d.setDate(dateList.get(i));
			d.setTitle("오늘의 기분은.." + i);
			d.setNotes("이런이런 일이 있었다..." + i);
			list.add(d);
		} 
		return list;
	}
	
	private void determineDateRange(SearchVO vo) {
		LocalDate targetDate = null;
		if (vo.getYear() != 0 && vo.getMonth() != 0 && vo.getDayOfMonth() != 0)
			targetDate = LocalDate.of(vo.getYear(), vo.getMonth(), vo.getDayOfMonth());
		else if (vo.getDate() != null && !vo.getDate().equals(""))
			targetDate = LocalDate.parse(vo.getDate());
		else 
			targetDate = LocalDate.now();
		
		
		int mFirst = targetDate.withDayOfMonth(1).getDayOfMonth();
		int mLast = targetDate.lengthOfMonth();
		String firstDayOfWeek = targetDate.withDayOfMonth(mFirst)
				.getDayOfWeek()
				.toString();		
		String lastDayOfWeek = targetDate.withDayOfMonth(mLast)
				.getDayOfWeek()
				.toString();
		
		List<DailyEntryVO> list = new ArrayList<>();
		int firstDateIndex = 0;
		int lastDateIndex = 0;
		for (int i=0; i<dayOfWeeks.length; i++) {
			if (firstDayOfWeek.equals(dayOfWeeks[i])) firstDateIndex = i;
			if (lastDayOfWeek.equals(dayOfWeeks[i])) lastDateIndex = i;
		}
		
		if (firstDateIndex > 0) { // 이전 달의 시작날짜
			LocalDate minusMonth = targetDate.minusMonths(1);
			int e = minusMonth.lengthOfMonth();
			int s = e-(firstDateIndex-1);
			String m = String.format("%02d", minusMonth.getMonthValue());
			int y = minusMonth.getYear();
			LocalDate temp = minusMonth.withDayOfMonth(s);
			for (int i=s; i<e; i++) {
				list.add(null);
			}
			vo.setStartDate(temp.getYear() 
					+"-"+ String.format("%02d", temp.getMonthValue()) 
					+"-"+ String.format("%02d", temp.getDayOfMonth()));
		} else { // 당월 시작날짜
			vo.setStartDate(targetDate.getYear() 
					+"-"+ String.format("%02d", targetDate.getMonthValue()) 
					+"-"+ String.format("%02d", targetDate.getDayOfMonth()));
		}
		
		if ((dayOfWeeks.length-(lastDateIndex+1)) > 0) { // 다음 달의 끝날짜
			LocalDate plusMonth = targetDate.plusMonths(1);
			LocalDate temp = plusMonth.withDayOfMonth((dayOfWeeks.length-(lastDateIndex+1)));
			vo.setEndDate(temp.getYear() 
					+"-"+ String.format("%02d", temp.getMonthValue()) 
					+"-"+ String.format("%02d", temp.getDayOfMonth()));
		} else { // 당월 끝날짜
			vo.setEndDate(targetDate.getYear() 
					+"-"+ String.format("%02d", targetDate.getMonthValue()) 
					+"-"+ String.format("%02d", targetDate.lengthOfMonth()));
		}
	}
}// class
