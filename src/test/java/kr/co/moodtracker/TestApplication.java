package kr.co.moodtracker;

import java.util.Calendar;

public class TestApplication {
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		
		System.out.println(
			  "\ngetWeekYear: " + c.getWeekYear()
			+ "\ngetFirstDayOfWeek: " + c.getFirstDayOfWeek()
			+ "\ngetTime: " + c.getTime()
			+ "\ndate(int): " + c.get(c.DATE)
			+ "\nweek(field): " + c.get(c.DAY_OF_WEEK)
			+ "\nmonth(int): " + (c.get(c.MONTH) + 1)
			+ "\nyear(int): " + c.get(c.YEAR)
				);
	}
	
}
