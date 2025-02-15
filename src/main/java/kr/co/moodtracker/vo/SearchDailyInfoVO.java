package kr.co.moodtracker.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDailyInfoVO extends SearchVO {
	
	private int dailyId;
	private int userId;
	private int noteId;
    private int moodId;
    private boolean byDate;
    private boolean byMoodLevel;
    private String noteTitle;
    private String noteContent;
    private Integer moodLevel;
    private String description;
    private String createdAt;
    private String updatedAt;
    /*----------------------------*/
    private List<ImageVO> imageList;
    
}
