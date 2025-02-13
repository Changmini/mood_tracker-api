package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailySearchVO extends SearchVO {
	
	private int dailyId;
	private int userId;
	private int noteId;
    private int moodId;
    private boolean byDate;
    private boolean byMoodLevel;
    
}
