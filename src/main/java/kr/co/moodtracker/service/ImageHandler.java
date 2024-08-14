package kr.co.moodtracker.service;

import java.util.Base64;
import java.util.List;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;

public class ImageHandler {
	
	public static void insertImageDataIntoDailyInfo(
			List<DailyInfoVO> images, List<DailyInfoVO> dailies) {
		int dailiesSize = dailies.size();
		int imagesSize = images.size();
		if (dailies == null || images == null || dailiesSize < 1 || imagesSize < 1)
			return ;// 비교 데이터가 없음
		int imagesIndex = 0;
		for (DailyInfoVO daily : dailies) {
			int dailyId = daily.getDailyId();
			DailyInfoVO image = images.get(imagesIndex);
			if (image !=null && image.getDailyId() == dailyId) {
				List<ImageVO> imageList = image.getImageList();
				for (ImageVO img : imageList) {
					String path = img.getImagePath();
					img.setImagePath(pathToBase64(path));
				}
				daily.setImageList(imageList);
				imagesIndex++;
			}// if
		}// foreach-dailies
		
	}
	
	public static String pathToBase64(String path) {
		if (path == null) 
			return path;
		return  Base64.getEncoder().encodeToString(path.getBytes());
	}
	
	public static String base64ToPath(String base64) {
		if (base64 == null) 
			return base64;
		byte[] byteArr = Base64.getDecoder().decode(base64);
		return new String(byteArr);
	}
}
