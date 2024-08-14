package kr.co.moodtracker.vo;

public class ImageVO {
	private int imageId;
	private String befImagePath;
	private String aftImagePath;
	private String imagePath;
	
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public String getBefImagePath() {
		return befImagePath;
	}
	public void setBefImagePath(String befImagePath) {
		this.befImagePath = befImagePath;
	}
	public String getAftImagePath() {
		return aftImagePath;
	}
	public void setAftImagePath(String aftImagePath) {
		this.aftImagePath = aftImagePath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
