package kr.co.moodtracker;

import java.time.LocalDate;
import java.util.Iterator;

public class TestApplication {
	
	public static void main(String[] args) {
//		LocalDate targetDate = LocalDate.now();
		int year=2024, month=2, date=18;
		LocalDate targetDate = LocalDate.of(year, month, date);
		LocalDate prevDate = targetDate.minusMonths(1);
		LocalDate nextDate = targetDate.plusMonths(1);
		
		
		
		System.out.println(
				"DayOfYear: " + targetDate.getDayOfYear() +"\n"+
				"DayOfMonth: " + targetDate.getDayOfMonth() +"\n"+
				"DayOfWeek: " + targetDate.getDayOfWeek() +"\n"+
				
				"Length: " + targetDate.lengthOfMonth() +"\n"
				);
	}
	
	public Object makeCalendar(LocalDate targetDate) {
		String[] dayOfWeek = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","PRIDAY","SATURDAY","SUNDAY"};
		
		int pivot = targetDate.getDayOfMonth();
		int end = targetDate.lengthOfMonth();
		for (int i = 1; i < pivot; i++) {
			
		}
		
		for (int i = pivot; i <= end; i++) {
			
		}
		
		return null;
	}
	
}
