package kr.co.moodtracker.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDailyInfoVO {

	private int dailyId;
    private String date;
    private String noteTitle;
    private String noteContent;
    private Integer moodLevel;
    private String description;
    /*----------------------------*/
    private List<ImageVO> imageList;
    
}
