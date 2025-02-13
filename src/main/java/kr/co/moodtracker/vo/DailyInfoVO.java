package kr.co.moodtracker.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyInfoVO extends ApiKeyVO {
	
	private int dailyId;
	private int userId;
	private int noteId;
    private int moodId;
    private String date;
    private String noteTitle;
    private String noteContent;
    private Integer moodLevel;
    private String description;
    private String createdAt;
    private String updatedAt;
    /*----------------------------*/
    private List<ImageVO> imageList;
	
}
