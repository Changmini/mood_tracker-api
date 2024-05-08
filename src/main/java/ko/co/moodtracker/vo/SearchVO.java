package ko.co.moodtracker.vo;

public class SearchVO {
	
	private int year;
	private int month;
	private int date;
	
	/* Format:  yyyy-mm-dd */
	private String startDate;
	private String endDate;
	
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "SearchVO [year=" + year + ", month=" + month + ", date=" + date + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
	
}
