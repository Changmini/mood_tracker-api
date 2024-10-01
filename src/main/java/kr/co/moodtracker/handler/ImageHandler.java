package kr.co.moodtracker.handler;

import java.util.Base64;
import java.util.List;

import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;

public class ImageHandler {
	
	private static final int PROFILE = 1;
	private static final int USER_ID = 2;
	
	/**
	 * <pre>
	 *   파라미터 dailies에 images 정보를 삽입하는 함수.
	 *   isAsc의 값이 true면 오름차순, false면 내림차순이다.
	 * </pre>
	 * @param images
	 * @param dailies
	 * @param isAsc
	 */
	public static void insertImageDataIntoDailyInfo(
			List<DailyInfoVO> images, List<DailyInfoVO> dailies, boolean isAsc) {
		int dailiesSize = dailies.size();
		int imagesSize = images.size();
		if (dailies == null || images == null || dailiesSize < 1 || imagesSize < 1)
			return ;// 비교 데이터가 없음
		int imagesIndex = isAsc ? 0 : imagesSize-1;
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
				imagesIndex = imagesIndex + (isAsc ? 1 : -1);
			}// if
		}// foreach-dailies
	}
	
	public static String convertBase64AndCheckAccessRights(String base64, int userId) 
			throws ImageLoadException {
		String imagePath = base64ToPath(base64);
		String[] pathArr = imagePath.split("\\/");
		if (Integer.valueOf(pathArr[USER_ID]) != userId)
			throw new ImageLoadException("접근 불가능한 사용자입니다.");
		return imagePath;
	}
	
	public static String convertBase64AndCheckProfileImage(String base64, int userId) 
			throws ImageLoadException {
		String imagePath = base64ToPath(base64);
		String[] pathArr = imagePath.split("\\/");
		if (!pathArr[PROFILE].equals("profile"))
			throw new ImageLoadException("접근 불가능한 이미지 유형입니다.");
		return imagePath;
	}
	
	public static String pathToBase64(String path) {
		if (path == null) 
			return path;
		return Base64.getEncoder()
				.encodeToString(removeRootPath(path).getBytes());
	}
	public static String removeRootPath(String imagePath) {
		// 보안상 서버의 전체 파일경로를 공개하면 안될 것 같아서..
		return imagePath.substring(FileHandler.rootPath().length());
	}
	
	public static String base64ToPath(String base64) {
		if (base64 == null) 
			return base64;
		byte[] byteArr = Base64.getDecoder().decode(base64);
		return new String(byteArr);
	}
}
