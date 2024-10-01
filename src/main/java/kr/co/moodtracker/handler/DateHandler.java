package kr.co.moodtracker.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;
import kr.co.moodtracker.vo.SearchVO;

public class DateHandler {
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final String[] DAY_OF_WEEKS = {
			"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"
		};
	public static final int[] DAY_OF_MONTH = {
			1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31
		};
	
	public static void determineDateRange(SearchVO vo) throws DataMissingException {
		LocalDate targetDate = null;
		if (vo.getYear() != 0 && vo.getMonth() != 0 && vo.getDayOfMonth() != 0) {
			targetDate = LocalDate.of(vo.getYear(), vo.getMonth(), vo.getDayOfMonth());
		} else if (vo.getDate() != null && !vo.getDate().equals("")) {
			String date = vo.getDate();
			int len = date.length();
			if (len == 7) targetDate = LocalDate.parse(date+"-01", FORMATTER); 
			else if (len == 10) targetDate = LocalDate.parse(date, FORMATTER);
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
		for (int i=0; i<DAY_OF_WEEKS.length; i++) {
			if (firstDayOfWeek.equals(DAY_OF_WEEKS[i])) firstDateIndex = i;
			if (lastDayOfWeek.equals(DAY_OF_WEEKS[i])) lastDateIndex = i;
		}
		
		if (firstDateIndex > 0) { // 이전 달의 시작날짜
			LocalDate minusMonth = targetDate.minusMonths(1);
			int e = minusMonth.lengthOfMonth();
			int s = e-(firstDateIndex-1);
			LocalDate temp = minusMonth.withDayOfMonth(s);
			vo.setStartDate(yyyy_MM_dd(temp, "-"));
		} else { // 당월 시작날짜
			LocalDate temp = targetDate.withDayOfMonth(1);
			vo.setStartDate(yyyy_MM_dd(temp, "-"));
		}
		
		if (lastDateIndex < DAY_OF_WEEKS.length-1) { // 다음 달의 끝날짜
			LocalDate plusMonth = targetDate.plusMonths(1);
			LocalDate temp = plusMonth.withDayOfMonth((DAY_OF_WEEKS.length-(lastDateIndex+1)));
			vo.setEndDate(yyyy_MM_dd(temp, "-"));
		} else { // 당월 끝날짜
			LocalDate temp = targetDate.withDayOfMonth(targetDate.lengthOfMonth());
			vo.setEndDate(yyyy_MM_dd(temp, "-"));
		}
	}
	
	public static List<DailyInfoVO> makeDateList(SearchVO vo, List<DailyInfoVO> dailies) {
		LocalDate startDate = LocalDate.parse(vo.getStartDate());
		LocalDate endDate = LocalDate.parse(vo.getEndDate());
		List<DailyInfoVO> list = new ArrayList<DailyInfoVO>();
		
		LocalDate nextDate = startDate;
		int dailesSize = dailies.size();
		int dailiesIndex = 0;
		/* 캘린더에 사용될 날짜 세팅하기 */
		while(!nextDate.isAfter(endDate)) {
			String nDate = nextDate.format(FORMATTER);
			String tmpDate = null;
			DailyInfoVO daily = null;
			if (dailesSize > dailiesIndex// DB에 저장된 날짜정보 세팅
					&& (daily = dailies.get(dailiesIndex)) != null
					&& (tmpDate = daily.getDate()) != null
					&& nDate.equals(tmpDate)) {
				dailiesIndex++;
			} else {// 기본 날짜정보만 세팅
				daily = new DailyInfoVO();
				daily.setDate(nDate);
			}
			list.add(daily);
			nextDate = nextDate.plusDays(1);
		}
		return list;
	}
	
	public static String today() {
		return yyyy_MM_dd(LocalDate.now(), "");
	}
	
	private static String yyyy_MM_dd(LocalDate date, String pattern) {
		return (date.getYear() 
			+pattern+ String.format("%02d", date.getMonthValue()) 
			+pattern+ String.format("%02d", date.getDayOfMonth())
		);
	}
}
