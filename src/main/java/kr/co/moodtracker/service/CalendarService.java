package kr.co.moodtracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ko.co.moodtracker.vo.DailyboxVO;
import ko.co.moodtracker.vo.SearchVO;

@Service
public class CalendarService {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String[] dayOfWeeks = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
	
	public List<DailyboxVO> getDailyboxOfTheMonth(SearchVO vo) {
		determineDateRange(vo);
		List<String> dateList = getDateList(vo);
		List<DailyboxVO> dailyboxList = testDailybox(dateList);
//		List<Dailybox> dailyboxList = repository.getDailyboxOfTheMonth();
		if (dailyboxList.size() < 1) return Collections.emptyList();
		return dailyboxList;
	}
	
	private List<DailyboxVO> testDailybox(List<String> dateList) {
		List<DailyboxVO> list = new ArrayList<DailyboxVO>();
		int size = dateList.size(); 
		for (int i=0; i<size; i++) {
			DailyboxVO d = new DailyboxVO();
			d.setDate(dateList.get(i));
			d.setTitle("오늘의 기분은.." + i);
			d.setContent("이런이런 일이 있었다..." + i);
			list.add(d);
		} 
		return list;
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
		
		List<DailyboxVO> list = new ArrayList<>();
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
