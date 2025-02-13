package kr.co.moodtracker.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVO extends ApiKeyVO {
	
	private int year;
	private int month;
	private int dayOfMonth;
	
	/* Format: yyyy-mm-dd */
	private String date;
	private String startDate;
	private String endDate;
	
	private String title;
	private String content; // noteContent만 검색하는 용도가 아닌 확장성을 고려한 이름 선정
	
	private int interval; // SECOND
	
    private int offset;
    private int limit=10;
    private List<Integer> ids;
	
}
