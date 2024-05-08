package kr.co.moodtracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ko.co.moodtracker.vo.DailyboxVO;
import ko.co.moodtracker.vo.SearchVO;

@Service
public class CalendarService {
	
	public List<DailyboxVO> getDailyboxOfTheMonth(SearchVO vo) {
//		LocalDate targetDate = LocalDate.of(
//				vo.getYear(), vo.getMonth(), vo.getDate()
//			);
//		vo = makeCalendar(targetDate, vo);
//		List<Dailybox> dailyboxList = repository.getDailyboxOfTheMonth();
//		if (dailyboxList.size() < 1) return Collections.emptyList();
//		return dailyboxList;
		return testDailybox();
	}
	
	private List<DailyboxVO> testDailybox() {
		String[] arr = {
			"2024-04-28","2024-04-29","2024-04-30","2024-05-01","2024-05-02","2024-05-03","2024-05-04"
			,"2024-05-05","2024-05-06","2024-05-07","2024-05-08","2024-05-09","2024-05-10","2024-05-11"
			,"2024-05-12","2024-05-13","2024-05-14","2024-05-15","2024-05-16","2024-05-17","2024-05-18"
			,"2024-05-19","2024-05-20","2024-05-21","2024-05-22","2024-05-23","2024-05-24","2024-05-25"
			,"2024-05-26","2024-05-27","2024-05-28","2024-05-29","2024-05-30","2024-05-31","2024-06-01"
		};
		List<DailyboxVO> list = new ArrayList<DailyboxVO>();
		for (int i=0; i<arr.length; i++) {
			DailyboxVO d = new DailyboxVO();
			d.setDate(arr[i]);
			d.setTitle("오늘의 기분은.." + i);
			d.setContent("이런이런 일이 있었다..." + i);
			list.add(d);
		}
		return list;
	}
	
	private SearchVO makeCalendar(LocalDate targetDate, SearchVO vo) {
		SearchVO searchVO = null;
		if (vo != null) searchVO = vo; 
		else searchVO = new SearchVO();
		
		String[] dayOfWeeks = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
		
		int mFirst = targetDate.withDayOfMonth(1).getDayOfMonth();
		int mLast = targetDate.lengthOfMonth();
		String firstDayOfWeek = targetDate.withDayOfMonth(mFirst)
				.getDayOfWeek()
				.toString();		
		String lastDayOfWeek = targetDate.withDayOfMonth(mLast)
				.getDayOfWeek()
				.toString();
		
		int firstDateIndex = 0;
		int lastDateIndex = 0;
		for (int i=0; i<dayOfWeeks.length; i++) {
			if (firstDayOfWeek.equals(dayOfWeeks[i])) firstDateIndex = i;
			if (lastDayOfWeek.equals(dayOfWeeks[i])) lastDateIndex = i;
		}
		
		if (firstDateIndex > 0) {
			LocalDate minusMonth = targetDate.minusMonths(1);
			LocalDate temp = minusMonth.withDayOfMonth(minusMonth.lengthOfMonth()-(firstDateIndex+1));
			searchVO.setStartDate(temp.getYear() 
					+"-"+ String.format("%02d", temp.getMonthValue()) 
					+"-"+ String.format("%02d", temp.getDayOfMonth()));
		} else {
			searchVO.setStartDate(targetDate.getYear() 
					+"-"+ String.format("%02d", targetDate.getMonthValue()) 
					+"-"+ String.format("%02d", targetDate.getDayOfMonth()));
		}
		
		if ((dayOfWeeks.length-(lastDateIndex+1)) > 0) {
			LocalDate plusMonth = targetDate.plusMonths(1);
			LocalDate temp = plusMonth.withDayOfMonth((dayOfWeeks.length-(lastDateIndex+1)));
			searchVO.setEndDate(temp.getYear() 
					+"-"+ String.format("%02d", temp.getMonthValue()) 
					+"-"+ String.format("%02d", temp.getDayOfMonth()));
		} else {
			searchVO.setEndDate(targetDate.getYear() 
					+"-"+ String.format("%02d", targetDate.getMonthValue()) 
					+"-"+ String.format("%02d", targetDate.lengthOfMonth()));
		}
		return searchVO;
	}
	
	
	public void getMemberSchedule() {
		
		return ;
	}
	
}
