package kr.co.moodtracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.SearchVO;

public class DateHandler {
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final String[] dayOfWeeks = {
			"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"
		};
	
	public static void determineDateRange(SearchVO vo) throws DataMissingException {
		LocalDate targetDate = null;
		if (vo.getYear() != 0 && vo.getMonth() != 0 && vo.getDayOfMonth() != 0) {
			targetDate = LocalDate.of(vo.getYear(), vo.getMonth(), vo.getDayOfMonth());
		} else if (vo.getDate() != null && !vo.getDate().equals("")) {
			String date = vo.getDate();
			int len = date.length();
			if (len == 7) targetDate = LocalDate.parse(date+"-01", formatter); 
			else if (len == 10) targetDate = LocalDate.parse(date, formatter);
		} else { 
			targetDate = LocalDate.now();
		}
		
		if (targetDate == null) 
			throw new DataMissingException("CalendarService: determineDateRange(): 객체 생성 실패");
		String firstDayOfWeek = targetDate.withDayOfMonth(1)
				.getDayOfWeek()
				.toString();		
		String lastDayOfWeek = targetDate.withDayOfMonth(targetDate.lengthOfMonth())
				.getDayOfWeek()
				.toString();
		
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
			LocalDate temp = minusMonth.withDayOfMonth(s);
			vo.setStartDate(temp.getYear() 
					+"-"+ String.format("%02d", temp.getMonthValue()) 
					+"-"+ String.format("%02d", temp.getDayOfMonth()));
		} else { // 당월 시작날짜
			vo.setStartDate(targetDate.getYear() 
					+"-"+ String.format("%02d", targetDate.getMonthValue()) 
					+"-"+ String.format("%02d", targetDate.getDayOfMonth()));
		}
		
		if (lastDateIndex < dayOfWeeks.length-1) { // 다음 달의 끝날짜
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
	
	public static List<DailyInfoVO> makeDateList(SearchVO vo, List<DailyInfoVO> dailies) {
		LocalDate s = LocalDate.parse(vo.getStartDate());
		LocalDate e = LocalDate.parse(vo.getEndDate());
		List<DailyInfoVO> list = new ArrayList<DailyInfoVO>();
		
		LocalDate tempDate = s;
		int dailiesIndex = 0;
		while(!tempDate.isAfter(e)) {
			String date = tempDate.format(DateHandler.formatter);
			String dailiesDate = null;
			DailyInfoVO d = null;
			DailyInfoVO ed = null;
			if (dailies.size() > dailiesIndex) {
				ed = dailies.get(dailiesIndex);
				dailiesDate = ed.getDate();
			}
			
			if (dailiesDate != null && date.equals(dailiesDate)) {
				d = ed;
				dailiesIndex++;
			} else {
				d = new DailyInfoVO();
				d.setDate(date);
			}
			list.add(d);
			tempDate = tempDate.plusDays(1);
		}
		return list;
	}
}
