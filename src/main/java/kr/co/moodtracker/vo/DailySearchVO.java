package kr.co.moodtracker.vo;

public class DailySearchVO extends SearchVO {
	private int dailyId;
	private int userId;
	private int noteId;
    private int moodId;
    private boolean byDate;
    private boolean byMoodLevel;
    
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
	public boolean isByDate() {
		return byDate;
	}
	public void setByDate(boolean byDate) {
		this.byDate = byDate;
	}
	public boolean isByMoodLevel() {
		return byMoodLevel;
	}
	public void setByMoodLevel(boolean byMoodLevel) {
		this.byMoodLevel = byMoodLevel;
	}
	
}
