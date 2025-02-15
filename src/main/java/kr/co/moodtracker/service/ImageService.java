package kr.co.moodtracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.handler.FileHandler;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.ImagesMapper;
import kr.co.moodtracker.vo.ImageVO;
import kr.co.moodtracker.vo.SearchDailyInfoVO;

@Service
public class ImageService {
	
	@Autowired
	ImagesMapper imagesMapper;

	public InputStreamResource getImage(String base64, int userId) 
			throws ImageLoadException, IOException {
		String imagePath = 
				ImageHandler.convertBase64AndCheckAccessRights(base64, userId);
		/**
		 * InputStreamResource를 사용하여 
		 * ResponseEntit...body(isr)에 해당 객체를 전달하면 메모리 누수는 없다
		 * ps. 자동으로 InputSream이 닫히도록 설계되어 있는 것 같다.
		 */
		FileInputStream fis = new FileInputStream(
				new File(FileHandler.rootPath() + imagePath));
		return new InputStreamResource(fis);
	}
	
	public InputStreamResource getProfileImage(String base64, int userId) 
			throws ImageLoadException, IOException {
		String imagePath = 
				ImageHandler.convertBase64AndCheckProfileImage(base64, userId);
		/**
		 * InputStreamResource를 사용하여 
		 * ResponseEntity...body(isr)에 해당 객체를 전달하면 메모리 누수는 없다
		 * ps. 자동으로 InputSream이 닫히도록 설계되어 있는 것 같다.
		 */
		FileInputStream fis = new FileInputStream(
				new File(FileHandler.rootPath() + imagePath));
		return new InputStreamResource(fis);
	}

	public void deleteImage(List<Integer> imageId, int userId) 
			throws DataNotDeletedException, DataMissingException {
		if (!(imageId != null && imageId.size() > 0)) 
			throw new DataMissingException("삭제할 이미지를 선택해주십시오.");
		SearchDailyInfoVO vo = new SearchDailyInfoVO();
		List<ImageVO> imgList = new ArrayList<>();
		for (Integer id : imageId) {
			ImageVO img = new ImageVO();
			img.setImageId(id);
			imgList.add(img);			
		}
		vo.setImageList(imgList);
		vo.setUserId(userId);
		int cnt = imagesMapper.deleteImage(vo);
		if (cnt == 0) 
			throw new DataNotDeletedException("이미지 삭제에 실패했습니다.");
	}
	
}
