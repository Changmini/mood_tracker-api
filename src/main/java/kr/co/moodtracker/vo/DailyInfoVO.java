package kr.co.moodtracker.vo;

import java.util.List;

public class DailyInfoVO {
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
    
	public int getDailyId() {
		return dailyId;
	}
	public void setDailyId(int dailyId) {
		this.dailyId = dailyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public int getMoodId() {
		return moodId;
	}
	public void setMoodId(int moodId) {
		this.moodId = moodId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNoteTitle() {
		return noteTitle;
	}
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public Integer getMoodLevel() {
		return moodLevel;
	}
	public void setMoodLevel(Integer moodLevel) {
		this.moodLevel = moodLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<ImageVO> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageVO> imageList) {
		this.imageList = imageList;
	}
	
}
