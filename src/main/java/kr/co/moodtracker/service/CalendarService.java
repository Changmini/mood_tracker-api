package kr.co.moodtracker.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class CalendarService {
	
	public Object getMonth(int year, int month) {
		return getMonth(year, month, 1);
	}
	
	public Object getMonth(int year, int month, int date) {
		if (year < 1 || month < 1 || date < 1) return null;
		
		LocalDate targetDate = LocalDate.of(year, month, date);
		

		return null;
	}
	
	
	public void getMemberSchedule() {
		
		return ;
	}
	
}
