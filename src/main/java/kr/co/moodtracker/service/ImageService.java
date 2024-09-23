package kr.co.moodtracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.handler.FileHandler;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.ImagesMapper;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;
import kr.co.moodtracker.vo.UserVO;

@Service
public class ImageService {
	
	@Autowired
	ImagesMapper imagesMapper;

	public byte[] getImage(String base64, int userId) 
			throws ImageLoadException, IOException {
		String imagePath = 
				ImageHandler.validateBase64ToPathConversion(base64, userId);
		try (FileInputStream fis = new FileInputStream(
				new File(FileHandler.rootPath() + imagePath))) {
			return fis.readAllBytes();
		}
	}

	public void deleteImage(List<Integer> imageId, int userId) 
			throws DataNotDeletedException, DataMissingException {
		if (!(imageId != null && imageId.size() > 0)) 
			throw new DataMissingException("삭제할 이미지를 선택해주십시오.");
		DailyInfoVO vo = new DailyInfoVO();
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
