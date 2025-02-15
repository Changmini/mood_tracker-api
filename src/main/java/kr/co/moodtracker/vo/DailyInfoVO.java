package kr.co.moodtracker.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyInfoVO extends ReturnDailyInfoVO {

	private int userId;
	private int noteId;
    private int moodId;
    /* ReturnDailyInfoVO ~ */
    private String createdAt;
    private String updatedAt;
    
    public ReturnDailyInfoVO returnDailyInfoVO() {
    	return new ReturnDailyInfoVO(
    				this.getDailyId()
    				,this.getDate()
    				,this.getNoteTitle()
    				,this.getNoteContent()
    				,this.getMoodLevel()
    				,this.getDescription()
    				,this.getImageList()
    			);
    }
    
}
